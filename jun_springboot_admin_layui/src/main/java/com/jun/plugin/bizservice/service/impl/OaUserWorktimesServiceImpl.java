package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaUserWorktimesMapper;
import com.jun.plugin.bizservice.entity.OaUserWorktimesEntity;
import com.jun.plugin.bizservice.service.OaUserWorktimesService;


@Service("oaUserWorktimesService")
public class OaUserWorktimesServiceImpl extends ServiceImpl<OaUserWorktimesMapper, OaUserWorktimesEntity> implements OaUserWorktimesService {


}