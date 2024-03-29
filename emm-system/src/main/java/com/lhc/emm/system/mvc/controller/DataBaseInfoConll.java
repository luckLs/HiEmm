package com.lhc.emm.system.mvc.controller;


import com.lhc.emm.system.mvc.mapper.DataBaseInfoMapper;
import com.lhc.emm.system.mvc.entity.DataBaseInfo;
import com.lhc.emm.system.mvc.mapper.DataBaseInfoMapper;
import com.lhc.emm.system.mvc.service.TableService;
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
    @PostMapping("/syncData")
    public void openSyncDataBaseInfo(@RequestBody DataBaseInfo info) {
        tableService.openSyncDataBaseInfo(info.getId());
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
