package com.zapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.Application;
import com.zapi.mapper.ApplicationMapper;
import com.zapi.service.ApplicationService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {
}