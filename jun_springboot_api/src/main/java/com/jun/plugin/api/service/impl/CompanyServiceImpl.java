package com.jun.plugin.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jun.plugin.api.core.AbstractService;
import com.jun.plugin.api.mapper.CompanyMapper;
import com.jun.plugin.api.model.Company;
import com.jun.plugin.api.service.CompanyService;

import javax.annotation.Resource;


/**
 * Created by Wujun on 2021/07/07.
 */
@Service
@Transactional
public class CompanyServiceImpl extends AbstractService<Company> implements CompanyService {
    @Resource
    private CompanyMapper companyMapper;

}
