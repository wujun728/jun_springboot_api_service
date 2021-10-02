package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.OaNotesInfoMapper;
import com.jun.plugin.bizservice.entity.OaNotesInfoEntity;
import com.jun.plugin.bizservice.service.OaNotesInfoService;


@Service("oaNotesInfoService")
public class OaNotesInfoServiceImpl extends ServiceImpl<OaNotesInfoMapper, OaNotesInfoEntity> implements OaNotesInfoService {


}