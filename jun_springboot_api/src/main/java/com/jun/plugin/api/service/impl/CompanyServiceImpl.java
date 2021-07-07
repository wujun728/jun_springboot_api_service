package com.jun.plugin.api.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jun.plugin.api.core.AbstractService;
import com.jun.plugin.api.mapper.CompanyMapper;
import com.jun.plugin.api.model.Company;
import com.jun.plugin.api.service.CompanyService;


/**
 * Created by Wujun on 2021/07/07.
 */
@Service
@Transactional
public class CompanyServiceImpl extends AbstractService<Company> implements CompanyService {
    @Resource
    private CompanyMapper companyMapper;

}
