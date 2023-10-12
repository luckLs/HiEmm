package com.lhc.newV.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhc.newV.system.mapper.ColumnMapper;
import com.lhc.newV.system.service.ColumnService;
import com.lhc.newV.system.entity.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column> implements ColumnService {


}