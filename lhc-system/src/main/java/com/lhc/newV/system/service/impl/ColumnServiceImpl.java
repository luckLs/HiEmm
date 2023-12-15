package com.lhc.newV.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhc.newV.system.entity.Column;
import com.lhc.newV.system.mapper.ColumnMapper;
import com.lhc.newV.system.service.ColumnService;

import com.lhc.newV.system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author luck
 */
@Service
@RequiredArgsConstructor
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements ColumnService {

    private final ColumnMapper columnMapper;

    @Override
    public void updateRelationshipLine(Integer id, Integer foreignKeyId) {
        Integer tableId = null;
        if (null != foreignKeyId) {
            tableId = this.getById(foreignKeyId).getTableId();
        }
        columnMapper.updateRelationshipLine(id, foreignKeyId, tableId);
    }
}