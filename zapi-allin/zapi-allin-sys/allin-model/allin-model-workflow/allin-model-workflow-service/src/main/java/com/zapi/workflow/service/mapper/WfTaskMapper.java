package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.workflow.service.model.entity.WfTask;
import org.apache.ibatis.annotations.Param;

/**
 * 工作流任务数据访问层接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 额外提供任务查询的特有方法
 */
public interface WfTaskMapper extends BaseMapper<WfTask> {
    
    /**
     * 分页查询用户任务
     * 根据处理人和任务状态查询任务列表
     * 支持分页，按创建时间倒序排列
     *
     * @param page 分页参数
     * @param assignee 任务处理人（用户ID或用户名）
     * @param status 任务状态（1-待处理，2-已完成，3-已驳回）
     * @return 分页的任务列表
     */
    IPage<WfTask> selectUserTasks(Page<WfTask> page, @Param("assignee") String assignee, @Param("status") Integer status);
    
    /**
     * 分页查询用户任务（按应用ID过滤）
     * 通过 JOIN 流程实例表来过滤指定应用的任务
     *
     * @param page 分页参数
     * @param assignee 任务处理人
     * @param status 任务状态
     * @param appId 应用ID
     * @return 分页的任务列表
     */
    IPage<WfTask> selectUserTasksByAppId(Page<WfTask> page, @Param("assignee") String assignee, @Param("status") Integer status, @Param("appId") String appId);
    
    /**
     * 分页查询用户任务（只查询 app_id 为 NULL 的任务）
     * 通过 JOIN 流程实例表来过滤通用任务
     *
     * @param page 分页参数
     * @param assignee 任务处理人
     * @param status 任务状态
     * @return 分页的任务列表
     */
    IPage<WfTask> selectUserTasksWithNullAppId(Page<WfTask> page, @Param("assignee") String assignee, @Param("status") Integer status);
}
    