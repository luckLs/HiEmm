package com.lhc.newV.system.controller;


import com.lhc.newV.system.entity.vo.ErTableVO;
import com.lhc.newV.system.entity.vo.TableColumnVO;
import com.lhc.newV.system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhc
 */
@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableConll {

    private final TableService tableService;


    @GetMapping
    public List<ErTableVO> get(@ModelAttribute TableColumnVO where) {
        return tableService.findList(where);
    }

   @GetMapping("/test")
   public String test() {
        return "Hello World";
    }

}
