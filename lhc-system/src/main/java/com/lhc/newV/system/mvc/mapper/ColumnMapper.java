package com.lhc.newV.system.mvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhc.newV.system.mvc.entity.Column;
import com.lhc.newV.system.mvc.entity.Table;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * 表表示数据表中的列 Mapper 接口
 *
 * @author luck
 */
public interface ColumnMapper extends BaseMapper<Column> {

    /**
     * 根据字段名称查找
     * @return Boolean
     */
    @Select("SELECT * FROM db_column WHERE database_info_id = #{databaseInfoId} AND name = #{name} limit 1")
    Column getColumnByName(@Param("databaseInfoId") Integer tableId, @Param("name") String name);

    /**
     * 更新主外键关系
     */
    @Update("update db_column set foreign_key_id = #{foreignKeyId},foreign_table_id=#{foreignTableId} where id = #{id}")
    void updateRelationshipLine(@Param("id") Integer id, @Param("foreignKeyId") Integer foreignKeyId, @Param("foreignTableId") Integer foreignTableId);

    @Delete("DELETE FROM db_column WHERE table_id IN (#{tableIds})")
    void delteColumnByTableIds(@Param("tableIds") List<Integer> tableIds);

}
