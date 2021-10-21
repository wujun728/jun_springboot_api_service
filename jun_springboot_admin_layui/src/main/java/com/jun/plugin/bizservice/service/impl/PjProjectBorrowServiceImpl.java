package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectBorrowMapper;
import com.jun.plugin.bizservice.entity.PjProjectBorrowEntity;
import com.jun.plugin.bizservice.service.PjProjectBorrowService;


@Service("pjProjectBorrowService")
public class PjProjectBorrowServiceImpl extends ServiceImpl<PjProjectBorrowMapper, PjProjectBorrowEntity> implements PjProjectBorrowService {


}