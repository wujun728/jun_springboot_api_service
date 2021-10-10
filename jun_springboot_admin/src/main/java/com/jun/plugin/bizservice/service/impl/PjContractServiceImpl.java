package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjContractMapper;
import com.jun.plugin.bizservice.entity.PjContractEntity;
import com.jun.plugin.bizservice.service.PjContractService;


@Service("pjContractService")
public class PjContractServiceImpl extends ServiceImpl<PjContractMapper, PjContractEntity> implements PjContractService {


}