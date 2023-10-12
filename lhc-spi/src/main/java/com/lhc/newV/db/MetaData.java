package com.lhc.newV.db;

import com.lhc.newV.db.model.MyTable;

import java.util.List;

/**
 * 获取数据库元数据信息
 * @author liaohaicheng
 */
public interface MetaData {


    /**
     * 查询所有表
     */
    List<MyTable> getTables(String url, String username, String password);


}