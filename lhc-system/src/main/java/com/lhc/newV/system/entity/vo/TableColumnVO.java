package com.lhc.newV.system.entity.vo;

import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class TableColumnVO {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表id
     */
    private Integer tableId;

    /**
     * 表描述
     */
    private String tableDescription;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 坐标X
     **/
    private Integer positionX;

    /**
     * 坐标Y
     **/
    private Integer positionY;

    /**
     * 列ID
     */
    private Integer columnId;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列别名
     */
    private String columnAlias;

    /**
     * 列描述
     */
    private String columnDescription;

    /**
     * 数据类型
     */
    private String dataType;


    /**
     * 是否为主键
     **/
    private Boolean isPrimaryKey;

    /**
     * 外键
     */
    private Integer foreignKeyId;

    /**
     * 外键_表id
     */
    private String foreignTableId;

    /**
     * 外键_表name
     */
    private String foreignKeyName;

    private HashSet<Integer> tableIds;
}
