package com.lhc.newV.system.mvc.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lhc.newV.system.mvc.entity.Table;
import com.lhc.newV.system.mvc.entity.vo.TableColumnVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 表的抽象 Mapper 接口
 */
public interface TableMapper extends BaseMapper<Table> {

    List<TableColumnVO> findList(@Param("tableIds") Set<Integer> tableIds);

    @Select("SELECT a.* FROM db_table a , db_column b,db_column c WHERE   a.id = b.table_id and b.id = c.foreign_key_id and c.foreign_key_id = ${foreignKeyId} LIMIT 1")
    Table getTableByForeignKeyId(Integer foreignKeyId);

    /**
     * 根据表id，查找所有关系表
     **/
    List<JSONObject> findForeignTablesByTableId(@Param("tableId") Integer tableIds);

    @Select("SELECT COUNT(1) > 0 FROM db_table WHERE database_info_id = #{databaseInfoId} AND name = #{name}")
    boolean getIsTable(@Param("databaseInfoId") String databaseInfoId, @Param("name") String name);
}
