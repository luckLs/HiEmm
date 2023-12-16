package com.lhc.newV.system.controller;

import com.lhc.newV.system.mapper.DataBaseInfoMapper;
import com.lhc.newV.system.mapper.SelectMapper;
import com.lhc.newV.system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/select")
@RequiredArgsConstructor
public class SelectConll {

    private final SelectMapper selectMapper;

    @GetMapping
    public List<Map<String, String>> getList(String type,String param1) {
        switch (type) {
            case "table": {
                return selectMapper.getTables(param1);
            }
            case "dbInfo": {
                return selectMapper.getDataBaseInfo();
            }
            default: {
                return null;
            }
        }
    }


}
