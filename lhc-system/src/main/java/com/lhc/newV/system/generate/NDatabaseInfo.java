package com.lhc.newV.system.generate;

import java.io.Serializable;

/**
 * n_database_info
 *
 * @author
 */
public class NDatabaseInfo implements Serializable {
    /**
     * 库 ID
     */
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
    private String connectAddress;

    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public String getConnectAddress() {
        return connectAddress;
    }

    public void setConnectAddress(String connectAddress) {
        this.connectAddress = connectAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}