package com.lhc.emm.db.sql;

import com.lhc.emm.db.Plugin;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liaohaicheng
 */
public class DBContext {

    public Map<String, Plugin> PLUGIN_MAP = new ConcurrentHashMap<>();
    {
        ServiceLoader<Plugin> s = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : s) {
            PLUGIN_MAP.put(plugin.getDbType(), plugin);
        }
    }
}
