package com.example.newv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 表表示数据表中的列
 **/
@Data
public class Column {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 库id
     **/
    private Integer databaseInfoId;

    /**
     * 表id
     **/
    private Integer tableId;

    /**
     * 列名
     **/
    private String name;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 是否为主键
     */
    private Boolean isPrimaryKey;

    /**
     * 列的别名
     */
    private String alias;

    /**
     * 描述
     */
    private String description;

    /**
     * 外键指向的表
     */
    private Integer foreignTableId;

    /**
     * 外键id
     */
    private Integer foreignKeyId;

}