package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectMapper;
import com.jun.plugin.bizservice.entity.PjProjectEntity;
import com.jun.plugin.bizservice.service.PjProjectService;


@Service("pjProjectService")
public class PjProjectServiceImpl extends ServiceImpl<PjProjectMapper, PjProjectEntity> implements PjProjectService {


}