package com.lhc.newV.db.model;

/**
 * 字段信息
 */
public class MyColumn {

    /**
     * 列名
     **/
    public String field;

    /**
     * 类型 比如：int
     **/
    public String type;

    /**
     * 描述
     **/
    public String collation;

    /**
     * 是否为null
     **/
    public String isNull;


    /**
     * 是否主键
     **/
    public Boolean isKey;

    /**
     * 默认值
     **/
    public String defVal;
    public String extra;
    public String privileges;
    public String comment;
}
