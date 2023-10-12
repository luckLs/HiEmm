package com.lhc.newV.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 表的抽象 Mapper 接口
 */
public interface TableMapper extends BaseMapper<Table> {

    List<TableColumnVO> findList(TableColumnVO where);

    @Select("SELECT a.* FROM n_table a , n_column b,n_column c WHERE   a.id = b.table_id and b.id = c.foreign_key_id and c.foreign_key_id = ${foreignKeyId} LIMIT 1")
    Table getTableByForeignKeyId(Integer foreignKeyId);
}
