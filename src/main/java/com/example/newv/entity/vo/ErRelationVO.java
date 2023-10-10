package com.example.newv.entity.vo;

import java.util.List;

/**
 * 字段er
 * @Email 1721316224@qq.com
 * @Author:liaohaicheng
 */
public class ErRelationVO {

    /**
     * 字段名
     */
    public String key;

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
     * 表之间的关系指向
     */
    public List<ErRelation> relation;

}