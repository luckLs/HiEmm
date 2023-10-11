package com.lhc.newV.system.controller;

import com.example.newv.entity.vo.ErTableVO;
import com.example.newv.entity.vo.TableColumnVO;
import com.example.newv.service.TableService;
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
public class TableConll {

    @Autowired
    TableService tableService;


    @GetMapping
    public List<ErTableVO> get(@ModelAttribute TableColumnVO where) {
        return tableService.findList(where);
    }

    @GetMapping("/test")
   public String test() {
        return "Hello World";
    }

}
