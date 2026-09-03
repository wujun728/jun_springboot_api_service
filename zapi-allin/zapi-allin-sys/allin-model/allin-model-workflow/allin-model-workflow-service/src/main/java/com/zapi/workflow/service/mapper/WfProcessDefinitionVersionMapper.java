package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workflow.service.model.entity.WfProcessDefinitionVersion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程定义版本记录数据访问层接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 额外提供流程定义版本特有的查询方法
 */
@Mapper
public interface WfProcessDefinitionVersionMapper extends BaseMapper<WfProcessDefinitionVersion> {
    
}