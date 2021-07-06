package com.company.project.service.impl;

import com.company.project.mapper.CompanyMapper;
import com.company.project.model.Company;
import com.company.project.service.CompanyService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
