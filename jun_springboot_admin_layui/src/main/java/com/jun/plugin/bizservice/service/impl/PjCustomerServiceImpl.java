package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjCustomerMapper;
import com.jun.plugin.bizservice.entity.PjCustomerEntity;
import com.jun.plugin.bizservice.service.PjCustomerService;


@Service("pjCustomerService")
public class PjCustomerServiceImpl extends ServiceImpl<PjCustomerMapper, PjCustomerEntity> implements PjCustomerService {


}