package com.lhc.newV.system.mvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhc.newV.system.mvc.entity.Column;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * 表表示数据表中的列 Mapper 接口
 *
 * @author luck
 */
public interface ColumnMapper extends BaseMapper<Column> {

    /**
     * 更新主外键关系
     */
    @Update("update db_column set foreign_key_id = #{foreignKeyId},foreign_table_id=#{foreignTableId} where id = #{id}")
    void updateRelationshipLine(@Param("id") Integer id, @Param("foreignKeyId") Integer foreignKeyId, @Param("foreignTableId") Integer foreignTableId);
}
