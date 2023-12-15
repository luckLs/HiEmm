package com.lhc.newV.system.controller;


import cn.hutool.core.bean.BeanUtil;
import com.lhc.newV.system.entity.Column;
import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import com.lhc.newV.system.entity.vo.TableVo;
import com.lhc.newV.system.service.ColumnService;
import com.lhc.newV.system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final ColumnService columnService;


    @GetMapping
    public Map<String, List<?>> get(@ModelAttribute TableColumnVO where) {
        return tableService.findList(where);
    }

    @GetMapping("list")
    public List<Table> getList() {
        return tableService.list();
    }


    @PostMapping("/saveTables")
    public void saveList(@RequestBody List<Table> saveVo) {
        tableService.saveOrUpdateBatch(saveVo);
    }

    /**
     * 更新主外键关系
     * @param column 字段
     */
    @PostMapping("/updateRelationshipLine")
    public void updateRelationshipLine(@RequestBody Column column) {
        columnService.updateRelationshipLine(column.getId(), column.getForeignKeyId());

    }

}
