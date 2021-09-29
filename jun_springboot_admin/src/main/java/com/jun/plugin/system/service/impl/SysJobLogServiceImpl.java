package com.jun.plugin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.plugin.system.entity.SysJobLogEntity;
import com.jun.plugin.system.mapper.SysJobLogMapper;
import com.jun.plugin.system.service.SysJobLogService;

import org.springframework.stereotype.Service;

/**
 * 定时任务 服务类
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Service("sysJobLogService")
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLogEntity> implements SysJobLogService {


}