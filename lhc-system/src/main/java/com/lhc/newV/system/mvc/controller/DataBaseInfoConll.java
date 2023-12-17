package com.lhc.newV.system.mvc.controller;


import com.lhc.newV.system.mvc.entity.DataBaseInfo;
import com.lhc.newV.system.mvc.mapper.DataBaseInfoMapper;
import com.lhc.newV.system.mvc.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author lhc
 */
@RestController
@RequestMapping("/dataBaseInfo")
@RequiredArgsConstructor
public class DataBaseInfoConll {

    private final TableService tableService;

    private final DataBaseInfoMapper dataBaseInfoMapper;


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
