package com.jun.plugin.api.service.impl;

import com.jun.plugin.api.mapper.CityMapper;
import com.jun.plugin.api.model.City;
import com.jun.plugin.api.service.CityService;
import com.jun.plugin.api.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by Wujun on 2021/07/07.
 */
@Service
@Transactional
public class CityServiceImpl extends AbstractService<City> implements CityService {
    @Resource
    private CityMapper cityMapper;

}
