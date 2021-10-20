package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectDailyMapper;
import com.jun.plugin.bizservice.entity.PjProjectDailyEntity;
import com.jun.plugin.bizservice.service.PjProjectDailyService;


@Service("pjProjectDailyService")
public class PjProjectDailyServiceImpl extends ServiceImpl<PjProjectDailyMapper, PjProjectDailyEntity> implements PjProjectDailyService {


}