package com.lhc.newV.system.common.util.dbUtil.Layout;

import java.util.*;

public class ERDiagram {
    public Map<String, Table> tables;

    public ERDiagram() {
        this.tables = new HashMap<>();
    }

    public void addTable(String tableName,int columnSize) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new Table(tableName,10));
        }
    }

    public void addRelationship(Object table1, Object table2) {
        Table t1 = tables.get(table1);
        Table t2 = tables.get(table2);

        if (t1 != null && t2 != null) {
            t1.addRelatedTable(t2);
            t2.addRelatedTable(t1);
        }
    }

    public void treeLayout() {
        // Find the table with the most relationships to use as the root
        Table root = null;
        int maxRelationships = 0;
        for (Table table : tables.values()) {
            int relationships = table.relatedTables.size();
            if (relationships > maxRelationships) {
                maxRelationships = relationships;
                root = table;
            }
        }

        // Set the root's coordinates
        root.setCoordinates(1, 1);

        // Set the coordinates for all other tables
        layoutTables(root, 1, 0);
    }

    private double layoutTables(Table table, int level, double startY) {
        double y = startY;
        for (Table relatedTable : table.relatedTables) {
            if (relatedTable.x == 0) {
                y += relatedTable.getHeight();
                relatedTable.setCoordinates(300 * level, y);
                y = layoutTables(relatedTable, level + 1, y + relatedTable.getHeight());
            }
        }
        return y;
    }

    // 获取计算后的坐标信息
    public Map<String, double[]> getTableCoordinates() {
        Map<String, double[]> coordinates = new HashMap<>();
        for (Table table : tables.values()) {
            coordinates.put(table.name, new double[]{table.x, table.y});
        }
        return coordinates;
    }
}