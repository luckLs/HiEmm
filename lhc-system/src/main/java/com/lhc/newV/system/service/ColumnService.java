package com.lhc.newV.system.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhc.newV.system.entity.Column;
import org.springframework.stereotype.Service;

@Service
public interface ColumnService extends IService<Column> {

    /**
     * 更新主外键关系
     * @param id 字段id
     * @param foreignKeyId 外键id
     */
    void updateRelationshipLine(Integer id,Integer foreignKeyId);

}