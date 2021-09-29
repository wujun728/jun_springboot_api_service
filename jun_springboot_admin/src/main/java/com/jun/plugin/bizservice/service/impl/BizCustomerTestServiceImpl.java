package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.BizCustomerTestMapper;
import com.jun.plugin.bizservice.entity.BizCustomerTestEntity;
import com.jun.plugin.bizservice.service.BizCustomerTestService;


@Service("bizCustomerTestService")
public class BizCustomerTestServiceImpl extends ServiceImpl<BizCustomerTestMapper, BizCustomerTestEntity> implements BizCustomerTestService {


}