package com.lhc.newV.system.common.util.dbUtil;

import com.lhc.newV.system.mvc.entity.Column;
import com.lhc.newV.system.mvc.entity.DataBaseInfo;

import java.util.List;
import java.util.Map;

/**
 * @author luck
 * 数据库连接外键表的工具类
 */
public class DBConnectForeignTablesUtil {
    public static final String _ID = "_id";
    public static final String ID = "id";


    public static void setForeignTable(DataBaseInfo dataBaseInfo, List<Column> columnList, Map<String, Object[]> tablePrimaryKeyMap) {
        String setFkRule = dataBaseInfo.getSetFkRule();
        if (null == setFkRule) {
            x_id(columnList, tablePrimaryKeyMap);
        } else {
            switch (dataBaseInfo.getSetFkRule()) {
                case _ID -> x_id(columnList, tablePrimaryKeyMap);
                case ID -> xid(columnList, tablePrimaryKeyMap);
                default -> x_id(columnList, tablePrimaryKeyMap);
            }
        }
    }

    /**
     * 表名_id处理
     */
    private static void x_id(List<Column> columnList, Map<String, Object[]> tablePrimaryKeyMap) {
        setfkid(columnList, tablePrimaryKeyMap, _ID);
    }

    /**
     * 表名id处理
     */
    private static void xid(List<Column> columnList, Map<String, Object[]> tablePrimaryKeyMap) {
        setfkid(columnList, tablePrimaryKeyMap, ID);
    }

    /**
     * 处理对应的外键
     */
    private static void setfkid(List<Column> columnList, Map<String, Object[]> tablePrimaryKeyMap, String id) {
        columnList.forEach(column -> {
            // 判断外键命名规则（表名id）的模式
            if (column.getName().endsWith(id)) {
                // 获得表名，如果有对应表则设置，该字段的外键表关系
                String foreignKeyTable = column.getName().substring(0, column.getName().length() - 3);
                if (null != tablePrimaryKeyMap.get(foreignKeyTable)) {
                    column.setForeignTableId((int) tablePrimaryKeyMap.get(foreignKeyTable)[0]);
                    column.setForeignKeyId((int) tablePrimaryKeyMap.get(foreignKeyTable)[1]);
                }
            }
        });
    }

}
