package com.lhc.newV.system.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhc.newV.system.mvc.entity.Table;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author luck
 */
@Service
public interface TableService extends IService<Table> {

    /**
     * ER图
     *
     * @param tableId         表id
     * @param otherTableIdIds 其它表id
     * @param relationLevel   查询出关系层级数
     * @return Er图
     */
    Map<String,List<?>> getEr(Integer tableId, String otherTableIdIds, Integer relationLevel);

    /**
     * 同步数据库
     **/
    void openSyncDataBaseInfo(Integer databaseInfoId);

}