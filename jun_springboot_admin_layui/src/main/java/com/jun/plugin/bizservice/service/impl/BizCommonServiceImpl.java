package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.BizCommonMapper;
import com.jun.plugin.bizservice.entity.BizCommonEntity;
import com.jun.plugin.bizservice.service.BizCommonService;


@Service("bizCommonService")
public class BizCommonServiceImpl extends ServiceImpl<BizCommonMapper, BizCommonEntity> implements BizCommonService {


}