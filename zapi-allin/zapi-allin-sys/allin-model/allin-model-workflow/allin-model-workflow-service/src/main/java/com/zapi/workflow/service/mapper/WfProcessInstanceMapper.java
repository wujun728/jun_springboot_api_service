package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workflow.service.model.entity.WfProcessInstance;

/**
 * 流程实例数据访问层接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 用于流程实例的数据库操作
 */
public interface WfProcessInstanceMapper extends BaseMapper<WfProcessInstance> {
}
    