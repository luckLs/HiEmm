package com.lhc.emm.system.mvc.entity.vo;

import java.util.List;

/**
 * 表的er图
 * @author luck
 */
public class ErTableVO {

    /**
     * 节id（表Id）
     **/
    public Integer id;

    /**
     * 节点名（表名）
     **/
    public String label;

    /**
     * 字段
     */
    public List<ErColumnVO> ports;


    /**
     * 表描述
     */
    public String tableDescription;

    /**
     * 表别名
     */
    public String tableAlias;

    /**
     * 坐标X
     **/
    public Integer positionX;

    /**
     * 坐标Y
     **/
    public Integer positionY;


}
