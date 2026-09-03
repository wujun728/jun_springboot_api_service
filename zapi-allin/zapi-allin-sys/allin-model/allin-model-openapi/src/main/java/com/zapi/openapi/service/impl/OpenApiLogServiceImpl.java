package com.zapi.openapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.openapi.entity.OpenApiLog;
import com.zapi.openapi.mapper.OpenApiLogMapper;
import com.zapi.openapi.service.OpenApiLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 接口调用日志Service实现类
 */
@Service
public class OpenApiLogServiceImpl extends ServiceImpl<OpenApiLogMapper, OpenApiLog> implements OpenApiLogService {

    @Override
    public Page<OpenApiLog> getLogPage(Integer current, Integer size, Long appId,
                                        Long configId, Integer callType, Integer status) {
        Page<OpenApiLog> page = new Page<>(current, size);
        return baseMapper.selectPageWithNames(page, appId, configId, callType, status);
    }

    @Override
    public OpenApiLog recordLog(Long configId, Long appId, Integer callType,
                                 String requestUrl, String requestMethod,
                                 String requestParams, String responseData,
                                 Integer status, String errorMsg,
                                 Integer syncCount, Long costTime, String clientIp) {
        OpenApiLog log = new OpenApiLog();
        log.setConfigId(configId);
        log.setAppId(appId);
        log.setCallType(callType);
        log.setRequestUrl(requestUrl);
        log.setRequestMethod(requestMethod);
        log.setRequestParams(requestParams);
        log.setResponseData(responseData);
        log.setStatus(status);
        log.setErrorMsg(errorMsg);
        log.setSyncCount(syncCount);
        log.setCostTime(costTime);
        log.setClientIp(clientIp);
        log.setCreateTime(LocalDateTime.now());
        save(log);
        return log;
    }

    @Override
    public int cleanHistoryLogs(int days) {
        QueryWrapper<OpenApiLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("create_time", LocalDateTime.now().minusDays(days));
        return baseMapper.delete(queryWrapper);
    }
}
