package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectInvoiceMapper;
import com.jun.plugin.bizservice.entity.PjProjectInvoiceEntity;
import com.jun.plugin.bizservice.service.PjProjectInvoiceService;


@Service("pjProjectInvoiceService")
public class PjProjectInvoiceServiceImpl extends ServiceImpl<PjProjectInvoiceMapper, PjProjectInvoiceEntity> implements PjProjectInvoiceService {


}