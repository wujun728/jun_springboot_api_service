package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;
import org.apache.ibatis.annotations.Select;

/**
 * 流程定义数据访问层接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 额外提供流程定义特有的查询方法
 */
public interface WfProcessDefinitionMapper extends BaseMapper<WfProcessDefinition> {
    
    /**
     * 根据流程标识查询最大版本号
     * 用于部署新版本流程时确定版本号
     * 新版本号 = 最大版本号 + 1
     *
     * @param key 流程标识，例如：LEAVE_APPROVAL
     * @return 该流程标识的最大版本号，如果不存在则返回null
     */
    @Select("select max(version) from wf_process_definition where `key` = #{key}")
    Integer selectMaxVersionByKey(String key);
}
    