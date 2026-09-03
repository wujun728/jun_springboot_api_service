package com.zapi.openapi.strategy.impl;

import com.zapi.openapi.entity.SyncDataReq;
import com.zapi.openapi.service.SysSyncService;
import com.zapi.openapi.strategy.DataSyncStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户数据同步策略
 */
@Component
public class UserSyncStrategy implements DataSyncStrategy {

    @Override
    public String fullSync(SyncDataReq request) {

        return "用户全量同步成功";
    }
    
    @Override
    public String incrementalSync(SyncDataReq request) {

        return "用户增量同步成功";
    }
    
    @Override
    public String getDataType() {
        return "user";
    }
}