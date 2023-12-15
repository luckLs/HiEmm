package com.lhc.newV.system.mapper;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * @author luck
 * 下拉选择
 */
public interface SelectMapper {

    /**
     * 选择表
     *
     * @return k-v-d
     */
    @Select("select id k, name v, description d from n_table")
    List<Map<String, String>> getTables();
}
