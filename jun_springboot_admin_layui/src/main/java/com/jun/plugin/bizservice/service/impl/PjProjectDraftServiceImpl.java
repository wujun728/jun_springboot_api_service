package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectDraftMapper;
import com.jun.plugin.bizservice.entity.PjProjectDraftEntity;
import com.jun.plugin.bizservice.service.PjProjectDraftService;


@Service("pjProjectDraftService")
public class PjProjectDraftServiceImpl extends ServiceImpl<PjProjectDraftMapper, PjProjectDraftEntity> implements PjProjectDraftService {


}