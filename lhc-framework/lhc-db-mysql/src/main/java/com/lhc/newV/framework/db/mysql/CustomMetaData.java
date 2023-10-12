package com.lhc.newV.framework.db.mysql;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import com.lhc.newV.db.MetaData;
import com.lhc.newV.db.jdbc.DefaultMetaService;
import com.lhc.newV.db.model.MyColumn;
import com.lhc.newV.db.model.MyTable;
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
            List<Entity> tableList = Db.use(ds).query("show table status");
            for (Entity table : tableList) {
                MyTable myTable = new MyTable();
                List<MyColumn> columns = new ArrayList<>();
                myTable.columnList = columns;
                myTable.name = table.getStr("name");
                myTable.description = table.getStr("comment");
                myTableList.add(myTable);

                // 3. 获取字段信息
                List<Entity> columnList = Db.use(ds).query("show full columns from " + myTable.name);
                for (Entity column : columnList) {
                    MyColumn myColumn = new MyColumn();
                    columns.add(myColumn);
                    myColumn.field = column.getStr("field");
                    myColumn.type = column.getStr("type");
                    myColumn.collation = column.getStr("collation");
                    myColumn.isNull = column.getStr("null");
                    myColumn.isKey = "PRI".equals(column.getStr("key"));
                    myColumn.defVal = column.getStr("default");
                    myColumn.extra = column.getStr("extra");
                    myColumn.privileges = column.getStr("privileges");
                    myColumn.comment = column.getStr("comment");
                }
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException("获取数据库表错误");
        }
        // 4. 关闭数据源
        ds.close();
        return myTableList;
    }
}
