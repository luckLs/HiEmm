package com.lhc.newV.system.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 表的抽象 Mapper 接口
 */
public interface TableMapper extends BaseMapper<Table> {

    List<TableColumnVO> findList(@Param("tableIds") Set<Integer> tableIds);

    @Select("SELECT a.* FROM n_table a , n_column b,n_column c WHERE   a.id = b.table_id and b.id = c.foreign_key_id and c.foreign_key_id = ${foreignKeyId} LIMIT 1")
    Table getTableByForeignKeyId(Integer foreignKeyId);

    /** 根据表id，查找所有关系表 **/
    List<JSONObject> findForeignTablesByTableId(@Param("tableId") Integer tableIds);
}
