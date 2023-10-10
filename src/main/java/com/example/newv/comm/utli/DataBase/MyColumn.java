package com.example.newv.comm.utli.DataBase;

/**
 * 字段信息
 */
public class MyColumn {

    /**
     * 列名
     **/
    public String field;

    /**
     * int(11)
     **/
    public String type;

    public String collation;

    /**
     * 是否为null
     **/
    public String isNull;
    public Boolean isKey;

    public String defVal;
    public String extra;
    public String privileges;
    public String comment;
}
