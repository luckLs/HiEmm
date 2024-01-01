package com.lhc.emm.system.mvc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author luck
 * 表表示数据表中的列
 */
@Data
@TableName("db_column")
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