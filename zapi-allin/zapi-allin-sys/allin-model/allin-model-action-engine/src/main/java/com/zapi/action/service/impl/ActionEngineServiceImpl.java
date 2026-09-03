package com.zapi.action.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.action.mapper.ActionEngineMapper;
import com.zapi.action.model.entity.ActionEngine;
import com.zapi.action.service.ActionEngineService;
import org.springframework.stereotype.Service;

/**
 * 动作执行引擎服务实现类
 * 
 * @author zapi
 * @date 2025-01-12
 */
@Service
public class ActionEngineServiceImpl extends ServiceImpl<ActionEngineMapper, ActionEngine> implements ActionEngineService {

}
