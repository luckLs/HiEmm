package com.lhc.newV.system.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.ErTableVO;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TableService extends IService<Table> {
    List<ErTableVO> findList(TableColumnVO where);

    void openSyncDataBaseInfo(Integer databaseInfoId);

}