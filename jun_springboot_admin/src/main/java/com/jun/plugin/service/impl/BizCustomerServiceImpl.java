package com.jun.plugin.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.mapper.BizCustomerMapper;
import com.jun.plugin.entity.BizCustomerEntity;
import com.jun.plugin.service.BizCustomerService;


@Service("bizCustomerService")
public class BizCustomerServiceImpl extends ServiceImpl<BizCustomerMapper, BizCustomerEntity> implements BizCustomerService {


}