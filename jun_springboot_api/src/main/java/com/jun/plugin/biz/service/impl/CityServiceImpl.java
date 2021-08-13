package com.jun.plugin.biz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jun.plugin.base.core.AbstractService;
import com.jun.plugin.biz.mapper.CityMapper;
import com.jun.plugin.biz.model.City;
import com.jun.plugin.biz.service.CityService;


/**
 * Created by Wujun on 2021/07/07.
 */
@Service
@Transactional
public class CityServiceImpl extends AbstractService<City> implements CityService {
    @Resource
    private CityMapper cityMapper;

}
