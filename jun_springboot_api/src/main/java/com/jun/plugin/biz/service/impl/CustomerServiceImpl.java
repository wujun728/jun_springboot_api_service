package com.jun.plugin.biz.service.impl;

import com.jun.plugin.biz.mapper.CustomerMapper;
import com.jun.plugin.biz.model.Customer;
import com.jun.plugin.biz.service.CustomerService;
import com.jun.plugin.base.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Wujun on 2021/08/14.
 */
@Service
@Transactional
public class CustomerServiceImpl extends AbstractService<Customer> implements CustomerService {
    @Resource
    private CustomerMapper customerMapper;

}
