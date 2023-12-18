package com.lhc.newV.framework.db.mysql;

import com.lhc.newV.db.MetaData;
import com.lhc.newV.db.Plugin;

/**
 * @author liaohaicheng
 */
public class CustomPlugin implements Plugin {
    @Override
    public String getDbType() {
        return "MySQL";
    }

    @Override
    public MetaData getMetaData() {
        return new CustomMetaData();
    }
}
