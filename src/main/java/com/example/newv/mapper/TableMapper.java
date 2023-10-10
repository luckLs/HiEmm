package com.example.newv.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newv.entity.Table;
import com.example.newv.entity.vo.TableColumnVO;
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
