package com.jun.plugin.biz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jun.plugin.base.core.AbstractService;
import com.jun.plugin.biz.mapper.CompanyMapper;
import com.jun.plugin.biz.model.Company;
import com.jun.plugin.biz.service.CompanyService;


/**
 * Created by Wujun on 2021/07/07.
 */
@Service
@Transactional
public class CompanyServiceImpl extends AbstractService<Company> implements CompanyService {
    @Resource
    private CompanyMapper companyMapper;

}
