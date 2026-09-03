package com.zapi.workflow.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.workflow.service.model.entity.WfApprovalRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程审批节点记录Mapper
 */
@Mapper
public interface WfApprovalRecordMapper extends BaseMapper<WfApprovalRecord> {
}
