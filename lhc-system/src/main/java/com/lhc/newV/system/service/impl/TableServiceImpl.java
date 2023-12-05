package com.lhc.newV.system.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.lhc.newV.system.entity.Column;
import com.lhc.newV.system.entity.DataBaseInfo;
import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.ErRelationVO;
import com.lhc.newV.system.entity.vo.ErColumnVO;
import com.lhc.newV.system.entity.vo.ErTableVO;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import com.lhc.newV.system.mapper.ColumnMapper;
import com.lhc.newV.system.mapper.DataBaseInfoMapper;
import com.lhc.newV.system.mapper.ForeignKeyMapper;
import com.lhc.newV.system.mapper.TableMapper;
import com.lhc.newV.system.service.TableService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    @Autowired
    ColumnServiceImpl columnServiceImpl;

    @Autowired
    DBContext dbConfig;

    @Resource
    ColumnMapper columnMapper;

    @Resource
    ForeignKeyMapper foreignKeyMapper;

    @Resource
    TableMapper tableMapper;

    @Resource
    DataBaseInfoMapper dataBaseInfoMapper;

    @Override
    public Map<String, List<?>> findList(TableColumnVO where) {
        List<Integer> findIds = new ArrayList<>();
        findIds.add(where.getTableId());
        HashSet<Integer> foreignTablesByTableId = this.findForeignTablesByTableId(findIds, new HashSet<>());
        where.setTableIds(foreignTablesByTableId);

        Map<String, ErTableVO> erTableMap = new HashMap<>();
        List<ErTableVO> erTableVOList = new ArrayList<>();
        List<ErRelationVO> erRelationVOList = new ArrayList<>();

        List<TableColumnVO> tableColumnVOList = tableMapper.findList(where);
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
        Map<String, List<?>> returnList = new HashMap<>();

        returnList.put("table", erTableVOList);
        returnList.put("relation", erRelationVOList);
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openSyncDataBaseInfo(Integer databaseInfoId) {
        // 获取数据库信息
        DataBaseInfo dataBaseInfo = dataBaseInfoMapper.selectById(databaseInfoId);
        // 获取数据库表信息
        MetaData metaData = dbConfig.PLUGIN_MAP.get(dataBaseInfo.getType()).getMetaData();
        List<MyTable> myTableList = metaData.getTables(dataBaseInfo.getJdbcUrl(), dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
        // 处理表和字段信息
        List<Column> columnList = new ArrayList<>();
        List<Table> tableList = new ArrayList<>();
        List<String> primaryKeyList = new ArrayList<>();
        // 主键tablePrimaryKeyMap--> K-表名:v主键
        Map<String, Object[]> tablePrimaryKeyMap = new HashMap<>();

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
        columnList.forEach(column -> {
            if (column.getName().endsWith("_id")) {
                String foreignKeyTable = column.getName().substring(0, column.getName().length() - 3);
                if (null != tablePrimaryKeyMap.get(foreignKeyTable)) {
                    column.setForeignTableId((int) tablePrimaryKeyMap.get(foreignKeyTable)[0]);
                    column.setForeignKeyId((int) tablePrimaryKeyMap.get(foreignKeyTable)[1]);
                }
            }
        });
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


    public HashSet<Integer> findForeignTablesByTableId(List<Integer> findIds,HashSet<Integer> haveIds){
        List<JSONObject> foreignTablesByTableId = tableMapper.findForeignTablesByTableId(findIds);
        for(JSONObject i : foreignTablesByTableId){
            findIds = new ArrayList<>();
            Integer tableId = i.getInt("tableId");
            Integer foreignTableId = i.getInt("foreignTableId");

            if(!haveIds.contains(tableId)){
                findIds.add(tableId);
            }

            if(!haveIds.contains(foreignTableId)){
                findIds.add(foreignTableId);
            }

            haveIds.add(tableId);
            haveIds.add(foreignTableId);
        };
        if(CollUtil.isNotEmpty(foreignTablesByTableId) && CollUtil.isNotEmpty(findIds)){
            findForeignTablesByTableId(findIds,haveIds);
        }
        return haveIds;
    }

}