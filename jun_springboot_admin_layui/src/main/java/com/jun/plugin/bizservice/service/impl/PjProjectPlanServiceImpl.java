package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectPlanMapper;
import com.jun.plugin.bizservice.entity.PjProjectPlanEntity;
import com.jun.plugin.bizservice.service.PjProjectPlanService;


@Service("pjProjectPlanService")
public class PjProjectPlanServiceImpl extends ServiceImpl<PjProjectPlanMapper, PjProjectPlanEntity> implements PjProjectPlanService {


}