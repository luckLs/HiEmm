package com.example.newv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.newv.entity.Table;
import com.example.newv.entity.vo.ErTableVO;
import com.example.newv.entity.vo.TableColumnVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TableService extends IService<Table> {
    List<ErTableVO> findList(TableColumnVO where);

    void openSyncDataBaseInfo(Integer databaseInfoId);

}