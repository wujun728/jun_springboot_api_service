package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.workflow.service.model.entity.WfCcRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 流程抄送记录Mapper
 */
@Mapper
public interface WfCcRecordMapper extends BaseMapper<WfCcRecord> {
    
    /**
     * 分页查询用户抄送记录（按应用ID过滤）
     * 通过JOIN流程实例表来过滤指定应用的抄送记录
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param appId 应用ID
     * @return 分页的抄送记录列表
     */
    IPage<WfCcRecord> selectUserCcRecordsByAppId(Page<WfCcRecord> page, @Param("userId") Long userId, @Param("appId") String appId);
    
    /**
     * 分页查询用户抄送记录（只查询app_id为NULL的记录）
     * 通过JOIN流程实例表来过滤通用流程的抄送记录
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页的抄送记录列表
     */
    IPage<WfCcRecord> selectUserCcRecordsWithNullAppId(Page<WfCcRecord> page, @Param("userId") Long userId);
}
