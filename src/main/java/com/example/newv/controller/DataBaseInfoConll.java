package com.example.newv.controller;

import com.example.newv.entity.DataBaseInfo;
import com.example.newv.mapper.DataBaseInfoMapper;
import com.example.newv.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lhc
 */
@RestController
@RequestMapping("/dataBaseInfo")
public class DataBaseInfoConll {

    @Autowired
    TableService tableService;

    @Resource
    DataBaseInfoMapper dataBaseInfoMapper;


    /**
     * 同步数据库
     **/
    @PostMapping("/sync/{id}")
    public void openSyncDataBaseInfo(@PathVariable Integer id) {
        tableService.openSyncDataBaseInfo(id);
    }

    /**
     * 删除数据库
     **/
    @DeleteMapping("/{id}")
    public void delDataBaseInfo(@PathVariable Integer id) {
        dataBaseInfoMapper.deleteById(id);
    }

    /**
     * 查询数据库
     **/
    @GetMapping
    public List<DataBaseInfo> getDataBaseInfo(@ModelAttribute DataBaseInfo dataBaseInfo) {
        return dataBaseInfoMapper.selectList(null);
    }

}
