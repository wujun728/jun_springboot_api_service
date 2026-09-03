package com.zapi.openapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.openapi.entity.OpenApiLog;

/**
 * 接口调用日志Service接口
 */
public interface OpenApiLogService extends IService<OpenApiLog> {

    /**
     * 分页查询日志列表
     */
    Page<OpenApiLog> getLogPage(Integer current, Integer size, Long appId,
                                 Long configId, Integer callType, Integer status);

    /**
     * 记录调用日志
     */
    OpenApiLog recordLog(Long configId, Long appId, Integer callType,
                          String requestUrl, String requestMethod,
                          String requestParams, String responseData,
                          Integer status, String errorMsg,
                          Integer syncCount, Long costTime, String clientIp);

    /**
     * 清理历史日志
     * @param days 保留天数
     */
    int cleanHistoryLogs(int days);
}
