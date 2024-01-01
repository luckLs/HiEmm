package com.lhc.emm.system.mvc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhc.emm.system.mvc.entity.Column;
import com.lhc.emm.system.mvc.entity.Column;
import com.lhc.emm.system.mvc.mapper.ColumnMapper;
import com.lhc.emm.system.mvc.service.ColumnService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lhc
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