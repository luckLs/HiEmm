package com.example.newv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 表表示外键关系
 **/
@Data
public class ForeignKey {

    /**
     * 外键表 ID
     **/
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 源列 ID
     **/
    private Integer fromColumnId;


    /**
     * 目标列 ID
     **/
    private Integer toColumnId;
}
