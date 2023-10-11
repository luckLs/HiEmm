package com.lhc.newV.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 表的抽象实体类
 **/
@Data
public class Table {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 库id
     **/
    private Integer databaseInfoId;

    /**
     * 表名
     **/
    private String name;

    /**
     * 描述
     **/
    private String description;


    private String alias;
}