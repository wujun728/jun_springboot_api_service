package com.zapi.openapi.strategy;

import com.zapi.openapi.entity.SyncDataReq;

/**
 * 数据同步策略接口
 */
public interface DataSyncStrategy {
    
    /**
     * 全量同步
     *
     * @param request 同步请求数据
     * @return 同步结果消息
     */
    String fullSync(SyncDataReq request);
    
    /**
     * 增量同步
     *
     * @param request 同步请求数据
     * @return 同步结果消息
     */
    String incrementalSync(SyncDataReq request);
    
    /**
     * 获取支持的数据类型
     *
     * @return 数据类型（user/role/dept）
     */
    String getDataType();
}