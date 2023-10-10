package com.example.newv.entity.vo;

import java.util.List;

/**
 * 表的er图
 */
public class ErTableVO {

    /**
     * 节id（表Id）
     **/
    public String id;

    /**
     * 节点名（表名）
     **/
    public String label;

    /**
     * 字段
     */
    public List<ErRelationVO> attrs;


    /**
     * 表描述
     */
    public String tableDescription;

    /**
     * 表别名
     */
    public String tableAlias;


}
