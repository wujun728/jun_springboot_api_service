package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectReportMapper;
import com.jun.plugin.bizservice.entity.PjProjectReportEntity;
import com.jun.plugin.bizservice.service.PjProjectReportService;


@Service("pjProjectReportService")
public class PjProjectReportServiceImpl extends ServiceImpl<PjProjectReportMapper, PjProjectReportEntity> implements PjProjectReportService {


}