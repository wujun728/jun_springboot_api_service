package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.BizMailMapper;
import com.jun.plugin.bizservice.entity.BizMailEntity;
import com.jun.plugin.bizservice.service.BizMailService;


@Service("bizMailService")
public class BizMailServiceImpl extends ServiceImpl<BizMailMapper, BizMailEntity> implements BizMailService {


}