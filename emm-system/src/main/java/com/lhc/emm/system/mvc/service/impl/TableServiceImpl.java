package com.lhc.emm.system.mvc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.lhc.emm.system.common.util.dbUtil.DBConnectForeignTablesUtil;
import com.lhc.emm.system.mvc.entity.Column;
import com.lhc.emm.system.mvc.entity.DataBaseInfo;
import com.lhc.emm.system.mvc.entity.vo.ErColumnVO;
import com.lhc.emm.system.mvc.entity.vo.ErRelationVO;
import com.lhc.emm.system.mvc.entity.vo.ErTableVO;
import com.lhc.emm.system.mvc.entity.vo.TableColumnVO;
import com.lhc.emm.system.mvc.mapper.ColumnMapper;
import com.lhc.emm.system.mvc.mapper.DataBaseInfoMapper;
import com.lhc.emm.system.mvc.mapper.TableMapper;
import com.lhc.emm.db.MetaData;
import com.lhc.emm.db.model.MyColumn;
import com.lhc.emm.db.model.MyTable;
import com.lhc.emm.db.sql.DBContext;
import com.lhc.emm.framework.db.mysql.utli.MD5Utli;
import com.lhc.emm.system.common.util.dbUtil.Layout.ERDiagram;
import com.lhc.emm.system.mvc.entity.Table;
import com.lhc.emm.system.mvc.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhc
 */
@Service
@RequiredArgsConstructor
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements TableService {

    private final DBContext dbConfig;
    private final TableMapper tableMapper;
    private final ColumnMapper columnMapper;
    private final DataBaseInfoMapper dataBaseInfoMapper;

    @Override
    public Map<String, List<?>> getEr(Integer tableId, String otherTableIdIds, Integer relationLevel) {
        // 根据表id，查找所有关系表
        Set<Integer> tableIds = null != tableId ? this.findForeignTablesByTableId(tableId, relationLevel) : new HashSet<>();

        // 其它表的id加入到查询
        if (StrUtil.isNotEmpty(otherTableIdIds)) {
            tableIds.addAll(Arrays.stream(otherTableIdIds.split(",")).map(Integer::parseInt).toList());
        }

        // 封装返回表的ER图关系
        Map<String, ErTableVO> erTableMap = new HashMap<>(100);
        List<ErTableVO> erTableVOList = new ArrayList<>();
        List<ErRelationVO> erRelationVOList = new ArrayList<>();
        List<TableColumnVO> tableColumnVOList = tableMapper.findList(tableIds);

        ERDiagram erDiagram = new ERDiagram();

        Map<Integer, TableColumnVO> tableColumnMap = tableColumnVOList.stream().collect(Collectors.toMap(TableColumnVO::getColumnId, v -> v, (v1, v2) -> v1));
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
            // 处理外键
            if (tableColumnVO.getForeignKeyId() != null) {
                erColumnVO.foreignTableId = tableColumnVO.getForeignTableId();
                if (tableColumnMap.get(tableColumnVO.getForeignKeyId()) != null) {
                    erColumnVO.foreignKeyName = tableColumnMap.get(tableColumnVO.getForeignKeyId()).getColumnName();
                    ErRelationVO erRelation = new ErRelationVO();
                    erRelation.id = tableColumnVO.getTableId() + "_" + tableColumnVO.getColumnId();
                    erRelation.staNode = tableColumnVO.getForeignKeyId();
                    erRelation.endNode = tableColumnVO.getColumnId();
                    erRelationVOList.add(erRelation);
                }
            }
            erTableVO.ports.add(erColumnVO);
        }

        // 设置xy轴
        if (erTableVOList.size() > 1) {
            erTableVOList.forEach(table -> erDiagram.addTable(table.id.toString(), table.ports.size()));
            erTableVOList.forEach(table -> table.ports.forEach(port -> {
                if (null != port.foreignTableId) {
                    erDiagram.addRelationship(table.id.toString(), port.foreignTableId.toString());
                }
            }));
            erDiagram.treeLayout();
            Map<String, double[]> coordinates = erDiagram.getTableCoordinates();
            erTableVOList.forEach(table -> {
                double[] coords = coordinates.get(table.id.toString());
                table.positionX = (int) coords[0];
                table.positionY = (int) coords[1];
            });
        }
        return Map.of("table", erTableVOList, "relation", erRelationVOList);
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
        // 主键tablePrimaryKeyMap--> K-表名:v-[表id 主键id],后续用来处理主外键关系
        Map<String, Object[]> tablePrimaryKeyMap = new HashMap<>(100);
        // 表有多少个字段
        Map<Integer, Integer> tableColumnSizeMap = new HashMap<>(100);

        // 查询原来的表
        List<Table> oldTableList = tableMapper.selectList(Wrappers.lambdaQuery(Table.class)
                .eq(Table::getDatabaseInfoId, dataBaseInfo.getId()).select(Table::getId, Table::getName));
        // 需要删除的表
        Map<String, Integer> deleteTableMap = new HashMap<>(100);
        if (CollUtil.isNotEmpty(oldTableList)) {
            deleteTableMap = oldTableList.stream().collect(Collectors.toMap(Table::getName, Table::getId));
        }

        for (MyTable myTable : myTableList) {
            // 保存表信息
            Table table = tableMapper.getTableByName(dataBaseInfo.getId(), myTable.name);
            if (null == table) {
                table = new Table();
                table.setDatabaseInfoId(dataBaseInfo.getId());
                table.setName(myTable.name);
                table.setAlias(MD5Utli.get_7(myTable.name));
            }
            table.setDescription(myTable.description);
            this.saveOrUpdate(table);

            // 集合中移除存在的字段，保留删除的字段
            deleteTableMap.remove(myTable.name);

            // 查询原来的字段
            List<Column> oldColumnList = columnMapper.selectList(Wrappers.lambdaQuery(Column.class)
                    .eq(Column::getTableId, table.getId()).select(Column::getId, Column::getName));
            // 需要删除的字段
            Map<String, Integer> deleteColumnMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(oldColumnList)) {
                deleteColumnMap = oldColumnList.stream().collect(Collectors.toMap(Column::getName, Column::getId));
            }

            //保存字段，遍历表中的字段信息
            for (MyColumn myColumn : myTable.columnList) {
                Column column = columnMapper.getColumnByName(table.getId(), myColumn.field);
                if (null == column) {
                    column = new Column();
                    column.setAlias(MD5Utli.get_7(myColumn.field));
                }
                column.setDatabaseInfoId(dataBaseInfo.getId());
                column.setTableId(table.getId());
                column.setName(myColumn.field);
                column.setDataType(myColumn.type);
                column.setIsPrimaryKey(myColumn.isKey);
                column.setDescription(myColumn.comment);
                Db.saveOrUpdate(column);
                columnList.add(column);
                // 处理主键，把主键放入map
                if (myColumn.isKey) {
                    tablePrimaryKeyMap.put(table.getName(), new Object[]{table.getId(), column.getId()});
                }

                // 集合中移除存在的字段，保留删除的字段
                deleteColumnMap.remove(column.getName());
            }

            // 删除字段
            if (CollUtil.isNotEmpty(deleteColumnMap)) {
                List<Integer> delteIds = new ArrayList<>(deleteColumnMap.values());
                columnMapper.deleteBatchIds(delteIds);
            }

            // 记录表有多少个字段
            tableColumnSizeMap.put(table.getId(), CollUtil.isEmpty(myTable.getColumnList()) ? 0 : myTable.getColumnList().size());

        }

        // 删除表 , 和关联的字段
        if (CollUtil.isNotEmpty(deleteTableMap)) {
            List<Integer> delteIds = new ArrayList<>(deleteTableMap.values());
            tableMapper.deleteBatchIds(delteIds);
            columnMapper.delteColumnByTableIds(delteIds);
        }

        // 处理columnList中字段的外键关系
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
    public Set<Integer> findForeignTablesByTableId(int tableId, Integer relationLevel) {
        if (relationLevel == null) {
            relationLevel = 3;
        }
        Set<Integer> visited = new HashSet<>();
        findForeignTablesRecursively(tableId, visited, relationLevel);
        return visited;
    }

    /**
     * 递归查找表的关系表
     **/
    private void findForeignTablesRecursively(int currentTableId, Set<Integer> visited, int relationLevel) {
        if (relationLevel <= 0) {
            return;
        }
        if (!visited.contains(currentTableId)) {
            visited.add(currentTableId);
            // 查询当前表的关系表
            List<JSONObject> foreignTables = tableMapper.findForeignTablesByTableId(currentTableId);
            for (JSONObject foreignTable : foreignTables) {
                findForeignTablesRecursively(foreignTable.getInt("tableId"), visited, relationLevel - 1);
                findForeignTablesRecursively(foreignTable.getInt("foreignTableId"), visited, relationLevel - 1);
            }
        }
    }


}