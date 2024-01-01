package com.lhc.emm.framework.db.mysql;

import com.lhc.emm.db.MetaData;
import com.lhc.emm.db.Plugin;

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
