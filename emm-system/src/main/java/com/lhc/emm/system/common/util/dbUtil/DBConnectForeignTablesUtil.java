package com.lhc.emm.system.common.util.dbUtil;

import com.lhc.emm.system.mvc.entity.Column;
import com.lhc.emm.system.mvc.entity.Column;
import com.lhc.emm.system.mvc.entity.DataBaseInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
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
        Map<String, Object[]> tempMap = new HashMap<>(tablePrimaryKeyMap.size());
        // 再处理一下表名 比如：db_user_info 只取user_info作为key
        tablePrimaryKeyMap.forEach((key, value) -> {
            int index = key.indexOf("_");
            if (index > 0) {
                String substring = key.substring(index + 1);
                if (!tablePrimaryKeyMap.containsKey(substring)) {
                    tempMap.put(key.substring(index + 1), value);
                }
            }
        });
        tablePrimaryKeyMap.putAll(tempMap);


        columnList.forEach(column -> {
            // 判断外键字段命名规则（表名id）的模式
            String columnName = column.getName();
            if (columnName.endsWith(id)) {
                // 截取方式，获得表名，如果有对应表则设置，该字段的外键表关系
                String foreignKeyTable = columnName.substring(0, columnName.length() - 3);
                Object[] table = tablePrimaryKeyMap.get(foreignKeyTable);
                if (null != table) {
                    column.setForeignTableId((int) table[0]);
                    column.setForeignKeyId((int) table[1]);
                }
            }
        });
    }
}
