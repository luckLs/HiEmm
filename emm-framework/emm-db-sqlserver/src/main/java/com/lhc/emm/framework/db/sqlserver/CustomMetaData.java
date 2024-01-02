package com.lhc.emm.framework.db.sqlserver;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import com.lhc.emm.db.MetaData;
import com.lhc.emm.db.jdbc.DefaultMetaService;
import com.lhc.emm.db.model.MyColumn;
import com.lhc.emm.db.model.MyTable;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaohaicheng
 */
public class CustomMetaData extends DefaultMetaService implements MetaData {
    @Override
    public List<MyTable> getTables(String url, String username, String password) {
        List<MyTable> myTableList = new ArrayList<>();
        // 1. 数据源配置
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        try {
            // 2. 获取表信息
            List<Entity> tableList = Db.use(ds).query("""
                    select 
                        t.name as name, 
                        ep.value as comment 
                    from 
                        sys.tables t 
                    left join sys.extended_properties ep on ep.major_id = t.object_id and ep.minor_id = 0 and ep.name = 'ms_description'
                    """);
            for (Entity table : tableList) {
                MyTable myTable = new MyTable();
                List<MyColumn> columns = new ArrayList<>();
                myTable.columnList = columns;
                myTable.name = table.getStr("name");
                myTable.description = table.getStr("comment");
                myTableList.add(myTable);

                // 3. 获取字段信息
                String columnQuery = String.format("""
                    select
                        c.column_name,
                        c.data_type,
                        c.character_maximum_length,
                        c.is_nullable,
                        k.column_name is_primary_key,
                        c.column_default,
                        ep.value as column_description
                    from
                        information_schema.columns c
                        left join information_schema.key_column_usage k on c.table_name = k.table_name and c.column_name = k.column_name
                        left join sys.extended_properties ep on ep.major_id = object_id(c.table_name) and ep.minor_id = c.ordinal_position and ep.name = 'ms_description'
                    where
                        c.table_name = '%s'
                order by
                    c.ordinal_position           
                """, myTable.name);
                List<Entity> columnList = Db.use(ds).query(columnQuery);
                for (Entity column : columnList) {
                    MyColumn myColumn = new MyColumn();
                    columns.add(myColumn);
                    myColumn.field = column.getStr("column_name");
                    myColumn.type = column.getStr("data_type") + "(" + column.getStr("character_maximum_length") + ")";
                    myColumn.collation = column.getStr("column_description");
                    myColumn.isNull = column.getStr("is_nullable");
                    myColumn.isKey = null != column.get("is_primary_key");
                    myColumn.defVal = column.getStr("column_default");
                }
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException("获取数据库表错误"+sqlException.getMessage());
        }
        // 4. 关闭数据源
        ds.close();
        return myTableList;
    }
}
