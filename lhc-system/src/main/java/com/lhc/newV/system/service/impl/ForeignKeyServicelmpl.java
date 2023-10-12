package com.lhc.newV.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhc.newV.system.entity.ForeignKey;
import com.lhc.newV.system.mapper.ForeignKeyMapper;
import com.lhc.newV.system.service.ForeignKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForeignKeyServicelmpl extends ServiceImpl<ForeignKeyMapper, ForeignKey> implements ForeignKeyService {

}