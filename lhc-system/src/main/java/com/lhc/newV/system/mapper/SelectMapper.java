package com.lhc.newV.system.mapper;

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
    @Select("select id k, name v, description d from n_table where database_info_id = #{param1}")
    List<Map<String, String>> getTables(String param1);

    @Select("select id k, name v, description d from n_data_base_info")
    List<Map<String, String>> getDataBaseInfo();
}
