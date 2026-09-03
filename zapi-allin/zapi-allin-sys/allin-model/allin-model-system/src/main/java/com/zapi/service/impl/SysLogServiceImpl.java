package com.zapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.SysLog;
import com.zapi.mapper.SysLogMapper;
import com.zapi.service.SysLogService;
import org.springframework.stereotype.Service;

/**
 * 系统日志Service实现类
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
} 