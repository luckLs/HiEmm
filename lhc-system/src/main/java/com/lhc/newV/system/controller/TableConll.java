package com.lhc.newV.system.controller;


import com.lhc.newV.system.entity.Column;
import com.lhc.newV.system.entity.Table;
import com.lhc.newV.system.service.ColumnService;
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
    private final ColumnService columnService;


    /**
     *  ER图
     * @param tableId 表id
     * @param otherTableIdIds 其他表id
     * @return Er图
     */
    @GetMapping("er")
    public Map<String, List<?>> getEr(Integer tableId,String otherTableIdIds) {
        return tableService.getEr(tableId,otherTableIdIds);
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
