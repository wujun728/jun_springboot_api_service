package com.jun.plugin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.plugin.entity.SysJobLogEntity;
import com.jun.plugin.mapper.SysJobLogMapper;
import com.jun.plugin.service.SysJobLogService;

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