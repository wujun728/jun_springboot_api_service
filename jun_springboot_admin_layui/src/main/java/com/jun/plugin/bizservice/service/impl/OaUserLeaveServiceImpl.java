package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaUserLeaveMapper;
import com.jun.plugin.bizservice.entity.OaUserLeaveEntity;
import com.jun.plugin.bizservice.service.OaUserLeaveService;


@Service("oaUserLeaveService")
public class OaUserLeaveServiceImpl extends ServiceImpl<OaUserLeaveMapper, OaUserLeaveEntity> implements OaUserLeaveService {


}