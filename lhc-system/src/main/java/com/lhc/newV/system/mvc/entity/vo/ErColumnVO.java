package com.lhc.newV.system.mvc.entity.vo;

/**
 * 字段er
 * @Email 1721316224@qq.com
 * @Author:liaohaicheng
 */
public class ErColumnVO {

    /**
     * id
     */
    public Integer id;

    /**
     * 字段名
     */
    public String name;

    /**
     * 字段类型
     */
    public String type;


    /**
     * 字段描述
     */
    public String desc;

    /**
     * 是否为主键
     */
    public Boolean isPrimaryKey;

    /**
     * 列别名
     */
    public String columnAlias;

    /**
     * 外键_表id
     */
    public Integer foreignTableId;

    /**
     * 外键_字段name
     */
    public String foreignKeyName;

}