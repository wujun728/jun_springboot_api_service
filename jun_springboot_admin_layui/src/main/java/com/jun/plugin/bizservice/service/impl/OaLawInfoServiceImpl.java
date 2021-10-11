package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaLawInfoMapper;
import com.jun.plugin.bizservice.entity.OaLawInfoEntity;
import com.jun.plugin.bizservice.service.OaLawInfoService;


@Service("oaLawInfoService")
public class OaLawInfoServiceImpl extends ServiceImpl<OaLawInfoMapper, OaLawInfoEntity> implements OaLawInfoService {


}