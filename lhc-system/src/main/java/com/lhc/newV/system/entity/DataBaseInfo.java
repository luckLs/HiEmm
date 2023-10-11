package com.lhc.newV.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * n_database_info
 *
 * @author lhc
 */
@Data
public class DataBaseInfo implements Serializable {
    /**
     * 库 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 库名
     */
    private String name;

    /**
     * 数据库类型
     */
    private String type;

    /**
     * 连接名
     */
    private String connectName;

    /**
     * 连接地址
     */
    private String JdbcUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}