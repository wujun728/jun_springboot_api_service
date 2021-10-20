package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaOfficeCountMapper;
import com.jun.plugin.bizservice.entity.OaOfficeCountEntity;
import com.jun.plugin.bizservice.service.OaOfficeCountService;


@Service("oaOfficeCountService")
public class OaOfficeCountServiceImpl extends ServiceImpl<OaOfficeCountMapper, OaOfficeCountEntity> implements OaOfficeCountService {


}