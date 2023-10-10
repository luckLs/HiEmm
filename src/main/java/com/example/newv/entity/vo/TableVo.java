package com.example.newv.entity.vo;

import com.example.newv.entity.Column;
import lombok.Data;

import java.util.List;

/**
 * 表Vo
 **/
@Data
public class TableVo {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableDescription;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 字段list
     **/
    private List<Column> columnList;


}