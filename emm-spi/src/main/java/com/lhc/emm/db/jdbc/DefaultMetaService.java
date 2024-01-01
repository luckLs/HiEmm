package com.lhc.emm.db.jdbc;

import com.lhc.emm.db.MetaData;
import com.lhc.emm.db.model.MyTable;

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