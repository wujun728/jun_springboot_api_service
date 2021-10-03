package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaLearnInfoMapper;
import com.jun.plugin.bizservice.entity.OaLearnInfoEntity;
import com.jun.plugin.bizservice.service.OaLearnInfoService;


@Service("oaLearnInfoService")
public class OaLearnInfoServiceImpl extends ServiceImpl<OaLearnInfoMapper, OaLearnInfoEntity> implements OaLearnInfoService {


}