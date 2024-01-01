package com.lhc.emm.system.mvc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * @author lhc
 * 通用的下拉选择
 */
public interface CommonSelectMapper {

    /**
     * 选择库
     * @return k-v-d
     */
    @Select("select id k, name v, description d from db_data_base_info")
    List<Map<String, String>> getDataBaseInfo();

    /**
     * 选择表
     * @return k-v-d
     */
    @Select("select id k, name v, description d from db_table where database_info_id = #{param1}")
    List<Map<String, String>> getTables(String param1);

}
