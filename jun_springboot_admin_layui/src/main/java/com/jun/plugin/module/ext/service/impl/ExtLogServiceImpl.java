package com.jun.plugin.module.ext.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.plugin.module.ext.entity.ExtLog;
import com.jun.plugin.module.ext.mapper.ExtLogMapper;
import com.jun.plugin.module.ext.service.IExtLogService;

/**
 * <p>
 * 日志 服务实现类
 * </p>
 *
 * @author laker
 * @since 2021-08-16
 */
@Service
public class ExtLogServiceImpl extends ServiceImpl<ExtLogMapper, ExtLog> implements IExtLogService {

}
