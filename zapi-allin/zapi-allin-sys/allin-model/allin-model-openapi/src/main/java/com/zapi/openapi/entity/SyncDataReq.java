package com.zapi.openapi.entity;

import com.zapi.workbench.entity.WbApplication;
import lombok.Data;

import java.util.Map;

/**
 * 数据同步请求实体类
 */
@Data
public class SyncDataReq {
    
    /**
     * 数据类型：user-用户 role-角色 dept-部门
     */
    private String dataType;
    
    /**
     * 配置UUID
     */
    private String configUuid;
    
    /**
     * 同步数据
     */
    private Map<String, Object> data;
    
    /**
     * 时间戳（增量同步使用）
     */
    private String timestamp;
    
    /**
     * 应用ID（由拦截器设置）
     */
    private Long appId;
    
    /**
     * 配置信息（由拦截器设置）
     */
    private OpenApiConfig config;
    
    /**
     * 应用信息（由拦截器设置）
     */
    private WbApplication application;
}
