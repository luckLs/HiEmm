package com.lhc.newV.db.jdbc;

import com.lhc.newV.db.MetaData;
import com.lhc.newV.db.model.MyTable;

import java.util.List;


/**
 * @author liaohaicheng
 */
public class DefaultMetaService implements MetaData {

    @Override
    public List<MyTable> getTables(String url, String username, String password) {
        return null;
    }
}