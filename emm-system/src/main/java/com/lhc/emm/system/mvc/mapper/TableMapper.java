package com.lhc.emm.system.mvc.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lhc.emm.system.mvc.entity.vo.TableColumnVO;
import com.lhc.emm.system.mvc.entity.Table;
import com.lhc.emm.system.mvc.entity.vo.TableColumnVO;
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
    List<JSONObject> findForeignTablesByTableId(@Param("tableId") Integer tableId);

    /**
     * 根据表名查找表
     * @return Boolean
     */
    @Select("SELECT * FROM db_table WHERE database_info_id = #{databaseInfoId} AND name = #{name} limit 1")
    Table getTableByName(@Param("databaseInfoId") Integer databaseInfoId, @Param("name") String name);
}
