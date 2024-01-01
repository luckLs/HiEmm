package com.lhc.emm.system.common.util.dbUtil.Layout;

import java.util.HashSet;
import java.util.Set;

public class Table {
    public String name;
    public int columnSize;
    public double x;
    public double y;
    public int height = 15;
    public Set<Table> relatedTables;

    public Table(String name,int columnSize) {
        this.name = name;
        this.columnSize = columnSize;
        this.relatedTables = new HashSet<>();
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void addRelatedTable(Table table) {
        this.relatedTables.add(table);
    }

    public int getHeight() {
        return this.height * (columnSize + 1);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

