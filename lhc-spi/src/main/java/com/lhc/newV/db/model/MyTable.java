package com.lhc.newV.db.model;

import lombok.Data;

import java.util.List;

/**
 * 表的信息
 **/
@Data
public class MyTable {

    /** 表名 **/
    public String name;

    /** 描述 **/
    public String description;

    /** 字段 **/
    public List<MyColumn> columnList;
}