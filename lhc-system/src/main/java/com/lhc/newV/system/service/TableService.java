package com.lhc.newV.system.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhc.newV.system.entity.Table;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author luck
 */
@Service
public interface TableService extends IService<Table> {

    /**
     *  ER图
     * @param tableId 表id
     * @param otherTableIdIds 其它表id
     * @return Er图
     */
    Map<String,List<?>> getEr(Integer tableId,String otherTableIdIds);

    /**
     * 同步数据库
     **/
    void openSyncDataBaseInfo(Integer databaseInfoId);

}