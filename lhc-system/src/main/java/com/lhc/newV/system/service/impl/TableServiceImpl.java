package com.lhc.newV.system.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.lhc.newV.db.MetaData;
import com.lhc.newV.db.model.MyColumn;
import com.lhc.newV.db.model.MyTable;
import com.lhc.newV.db.sql.DBContext;
import com.lhc.newV.framework.db.mysql.CustomMetaData;
import com.lhc.newV.framework.db.mysql.utli.MD5Utli;
import com.lhc.newV.system.entity.Column;
import com.lhc.newV.system.entity.DataBaseInfo;
import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.ErRelation;
import com.lhc.newV.system.entity.vo.ErRelationVO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<ErTableVO> findList(TableColumnVO where) {
        List<TableColumnVO> tableColumnVOList = tableMapper.findList(where);
        Map<String, ErTableVO> tableVoMap = tableColumnVOList.stream()
                .collect(Collectors.groupingBy(TableColumnVO::getTableName,
                        Collectors.collectingAndThen(Collectors.toList(), values -> {
                            ErTableVO tableVo = new ErTableVO();
                            tableVo.id = values.get(0).getTableId();
                            tableVo.label = values.get(0).getTableName();
                            //tableVo.tableDescription = values.get(0).getTableDescription();
                            //tableVo.tableAlias = values.get(0).getTableAlias();
                            tableVo.attrs = values.stream()
                                    .map(tableColumnVO -> {
                                        ErRelationVO r = new ErRelationVO();
                                        r.key = tableColumnVO.getColumnName();
                                        r.desc = tableColumnVO.getColumnDescription();
                                        r.type = tableColumnVO.getDataType();
                                        r.isPrimaryKey = tableColumnVO.getIsPrimaryKey();
                                        r.columnAlias = tableColumnVO.getColumnAlias();

                                        return r;
                                    })
                                    .collect(Collectors.toList());
                            return tableVo;
                        })));
//        //a();
        return new ArrayList<>(convertToErTableVOList(tableColumnVOList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openSyncDataBaseInfo(Integer databaseInfoId) {
        // 获取数据库信息
        DataBaseInfo dataBaseInfo = dataBaseInfoMapper.selectById(databaseInfoId);
        // 获取数据库表信息
        MetaData metaData = dbConfig.PLUGIN_MAP.get(dataBaseInfo.getType()).getMetaData();
        List<MyTable> myTableList =  metaData.getTables(dataBaseInfo.getJdbcUrl(), dataBaseInfo.getUserName(), dataBaseInfo.getPassword());
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

            // 现在有几张表如下
            //StringBuilder aiCorpus = new StringBuilder();
            //aiCorpus.append(".表：").append(table.getName());
            //aiCorpus.append("字段有：");
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
                //aiCorpus.append("[" + column.getAlias() + "]");
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




    public static List<ErTableVO> convertToErTableVOList(List<TableColumnVO> tableColumnVOList) {
        Map<String, ErTableVO> erTableVOMap = new HashMap<>();

        for (TableColumnVO columnVO : tableColumnVOList) {
            ErTableVO erTableVO = erTableVOMap.computeIfAbsent(
                    columnVO.getTableName(),
                    tableName -> {
                        ErTableVO newErTableVO = new ErTableVO();
                        newErTableVO.id = columnVO.getTableId();
                        newErTableVO.label = columnVO.getTableName();
                        newErTableVO.tableDescription = columnVO.getTableDescription();
                        newErTableVO.tableAlias = columnVO.getTableAlias();
                        newErTableVO.attrs = new ArrayList<>();
                        return newErTableVO;
                    }
            );

            ErRelationVO erRelationVO = new ErRelationVO();
            erRelationVO.key = columnVO.getColumnName();
            erRelationVO.type = columnVO.getDataType();
            erRelationVO.desc = columnVO.getColumnDescription();
            erRelationVO.isPrimaryKey = columnVO.getIsPrimaryKey();
            erRelationVO.columnAlias = columnVO.getColumnAlias();

            if (columnVO.getForeignKeyId() != null) {
                ErRelation erRelation = new ErRelation();
                erRelation.key = "id";
                erRelation.nodeId = columnVO.getForeignKeyName();
                erRelationVO.relation = List.of(erRelation);
            }

            erTableVO.attrs.add(erRelationVO);
        }

        return new ArrayList<>(erTableVOMap.values());
    }

}