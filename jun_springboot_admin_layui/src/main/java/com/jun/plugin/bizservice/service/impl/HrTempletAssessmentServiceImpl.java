package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.HrTempletAssessmentMapper;
import com.jun.plugin.bizservice.entity.HrTempletAssessmentEntity;
import com.jun.plugin.bizservice.service.HrTempletAssessmentService;


@Service("hrTempletAssessmentService")
public class HrTempletAssessmentServiceImpl extends ServiceImpl<HrTempletAssessmentMapper, HrTempletAssessmentEntity> implements HrTempletAssessmentService {


}