package com.lhc.newV.system.mvc.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.lhc.newV.db.MetaData;
import com.lhc.newV.db.model.MyColumn;
import com.lhc.newV.db.model.MyTable;
import com.lhc.newV.db.sql.DBContext;
import com.lhc.newV.framework.db.mysql.utli.MD5Utli;
import com.lhc.newV.system.mvc.entity.Column;
import com.lhc.newV.system.mvc.entity.DataBaseInfo;
import com.lhc.newV.system.mvc.entity.Table;
import com.lhc.newV.system.mvc.entity.vo.ErRelationVO;
import com.lhc.newV.system.mvc.entity.vo.ErColumnVO;
import com.lhc.newV.system.mvc.entity.vo.ErTableVO;
import com.lhc.newV.system.mvc.entity.vo.TableColumnVO;
import com.lhc.newV.system.mvc.mapper.ColumnMapper;
import com.lhc.newV.system.mvc.mapper.DataBaseInfoMapper;
import com.lhc.newV.system.mvc.mapper.TableMapper;
import com.lhc.newV.system.mvc.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.lhc.newV.system.common.util.dbUtil.DBConnectForeignTablesUtil;

/**
 * @author luck
 */
@Service
@RequiredArgsConstructor
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    private final DBContext dbConfig;
    private final ColumnMapper columnMapper;
    private final TableMapper tableMapper;
    private final DataBaseInfoMapper dataBaseInfoMapper;

    @Override
    public Map<String, List<?>> getEr(Integer tableId, String otherTableIdIds) {
        // 根据表id，查找所有关系表
        Set<Integer> tableIds = new HashSet<>();

        if (null != tableId) {
            tableIds = this.findForeignTablesByTableId(tableId);
        }


        // 其它表的id加入到查询
        if (StrUtil.isNotEmpty(otherTableIdIds) && tableIds != null) {
            tableIds.addAll(Arrays.stream(otherTableIdIds.split(",")).map(Integer::parseInt).toList());
        }

        // 封装返回表的ER图关系
        Map<String, ErTableVO> erTableMap = new HashMap<>(100);
        List<ErTableVO> erTableVOList = new ArrayList<>();
        List<ErRelationVO> erRelationVOList = new ArrayList<>();

        List<TableColumnVO> tableColumnVOList = tableMapper.findList(tableIds);
        for (TableColumnVO tableColumnVO : tableColumnVOList) {
            String tableName = tableColumnVO.getTableName();
            ErTableVO erTableVO = erTableMap.getOrDefault(tableName, new ErTableVO());
            if (erTableVO.id == null) {
                erTableVO.id = tableColumnVO.getTableId();
                erTableVO.label = tableColumnVO.getTableName();
                erTableVO.positionX = tableColumnVO.getPositionX();
                erTableVO.positionY = tableColumnVO.getPositionY();
                erTableVO.tableDescription = tableColumnVO.getTableDescription();
                erTableVO.tableAlias = tableColumnVO.getTableAlias();
                erTableVO.ports = new ArrayList<>();
                erTableMap.put(tableName, erTableVO);
                erTableVOList.add(erTableVO);
            }
            ErColumnVO erColumnVO = new ErColumnVO();
            erColumnVO.id = tableColumnVO.getColumnId();
            erColumnVO.name = tableColumnVO.getColumnName();
            erColumnVO.desc = tableColumnVO.getColumnDescription();
            erColumnVO.type = tableColumnVO.getDataType();
            erColumnVO.isPrimaryKey = tableColumnVO.getIsPrimaryKey();
            erColumnVO.columnAlias = tableColumnVO.getColumnAlias();
            erTableVO.ports.add(erColumnVO);
            if (tableColumnVO.getForeignKeyId() != null) {
                ErRelationVO erRelation = new ErRelationVO();
                erRelation.id = tableColumnVO.getTableId() + "_" + tableColumnVO.getColumnId();
                erRelation.staNode = tableColumnVO.getForeignKeyId();
                erRelation.endNode = tableColumnVO.getColumnId();
                erRelationVOList.add(erRelation);
            }
        }
        Map<String, List<?>> returnList = new HashMap<>(2);
        returnList.put("table", erTableVOList);
        returnList.put("relation", erRelationVOList);
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openSyncDataBaseInfo(Integer databaseInfoId) {
        // 获取数据库信息
        DataBaseInfo dataBaseInfo = dataBaseInfoMapper.selectById(databaseInfoId);
        // 根据数据库type类型，获取数据库表信息
        MetaData metaData = dbConfig.PLUGIN_MAP.get(dataBaseInfo.getType()).getMetaData();
        List<MyTable> myTableList = metaData.getTables(dataBaseInfo.getJdbcUrl(), dataBaseInfo.getUserName(), dataBaseInfo.getPassword());

        // 处理表和字段信息
        List<Column> columnList = new ArrayList<>();
        List<String> primaryKeyList = new ArrayList<>();
        // 主键tablePrimaryKeyMap--> K-表名:v-[表id 主键id]
        Map<String, Object[]> tablePrimaryKeyMap = new HashMap<>(100);

        for (MyTable myTable : myTableList) {
            // 保存表信息
            Table table = new Table();
            table.setName(myTable.name);
            table.setDescription(myTable.description);
            table.setDatabaseInfoId(dataBaseInfo.getId());
            table.setAlias(MD5Utli.get_7(myTable.name));
            this.save(table);

            // 遍历表中的字段信息
            for (MyColumn myColumn : myTable.columnList) {
                // 保存字段信息
                Column column = new Column();
                column.setDatabaseInfoId(dataBaseInfo.getId());
                column.setTableId(table.getId());
                column.setName(myColumn.field);
                column.setDataType(myColumn.type);
                column.setIsPrimaryKey(myColumn.isKey);
                column.setDescription(myColumn.comment);
                column.setAlias(MD5Utli.get_7(myColumn.field));
                Db.save(column);

                columnList.add(column);
                // 处理主键
                if (myColumn.isKey) {
                    primaryKeyList.add(myColumn.field);
                    tablePrimaryKeyMap.put(table.getName(), new Object[]{table.getId(), column.getId()});
                }
            }
        }

        // 处理外键关系
        DBConnectForeignTablesUtil.setForeignTable(dataBaseInfo, columnList, tablePrimaryKeyMap);
        Db.updateBatchById(columnList);

    }



    public void a() {
        List<Table> tables = tableMapper.selectList(new QueryWrapper<>());
        Map tabMap = new HashMap<>();
        tables.forEach(tab -> {
            List<Map> colMap = new ArrayList<>();
            tabMap.put(tab.getAlias() + "表", colMap);
            List<Column> columns = columnMapper.selectList(new LambdaQueryWrapper<Column>().eq(Column::getTableId, tab.getId()));
            columns.forEach(col -> {
                Map map = new HashMap<>();
                colMap.add(map);
                if (null == col.getForeignKeyId()) {
                    map.put(col.getIsPrimaryKey() ? "主键" : "普通字段", col.getAlias());
                } else {
                    Table tableByForeignKeyId = tableMapper.getTableByForeignKeyId(col.getForeignKeyId());
                    map.put("外键", col.getAlias() + ",指向的是" + tableByForeignKeyId.getAlias() + "表的主键]");
                }
            });
        });
        System.out.println(JSONUtils.toJSONString(tabMap));
    }

    /**
     * 根据表id，查找所有关系表
     **/
    public Set<Integer> findForeignTablesByTableId(int tableId) {
        Set<Integer> visited = new HashSet<>();
        findForeignTablesRecursively(tableId, visited);
        return visited;
    }

    /**
     * 递归查找表的关系表
     **/
    private void findForeignTablesRecursively(int currentTableId, Set<Integer> visited) {
        if (!visited.contains(currentTableId)) {
            visited.add(currentTableId);
            // 查询当前表的关系表
            List<JSONObject> foreignTables = tableMapper.findForeignTablesByTableId(currentTableId);
            for (JSONObject foreignTable : foreignTables) {
                findForeignTablesRecursively(foreignTable.getInt("tableId"), visited);
                findForeignTablesRecursively(foreignTable.getInt("foreignTableId"), visited);
            }
        }
    }



}