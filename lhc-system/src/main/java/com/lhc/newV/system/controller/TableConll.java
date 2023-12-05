package com.lhc.newV.system.controller;


import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import com.lhc.newV.system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * 表Api
 */
@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableConll {

    private final TableService tableService;


    @GetMapping
    public Map<String,List<?>> get(@ModelAttribute TableColumnVO where) {
        return tableService.findList(where);
    }

    @PostMapping("/list")
    public void saveList(@RequestBody List<Table> saveVo) {
        tableService.saveOrUpdateBatch(saveVo);
    }

   @GetMapping("/test")
   public String test() {
        return "Hello World";
    }

}
