package com.zapi.listener;

import com.zapi.entity.SysLog;
import com.zapi.event.LogEvent;
import com.zapi.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听器
 * 用于处理日志事件并保存到数据库
 */
@Component
@Slf4j
public class LogEventListener {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 异步处理日志事件
     */
    @EventListener
    @Async
    public void handleLogEvent(LogEvent logEvent) {
        try {
            // 将LogEvent转换为SysLog实体
            SysLog sysLog = new SysLog();
            sysLog.setUsername(logEvent.getUsername());
            sysLog.setUserId(logEvent.getUserId());
            sysLog.setOperation(logEvent.getOperation());
            sysLog.setMethod(logEvent.getMethod());
            sysLog.setParams(logEvent.getParams());
            sysLog.setIp(logEvent.getIp());
            sysLog.setUrl(logEvent.getUrl());
            sysLog.setHttpMethod(logEvent.getHttpMethod());
            sysLog.setExecutionTime(logEvent.getExecutionTime());
            sysLog.setResult(logEvent.getResult());
            sysLog.setErrorMessage(logEvent.getErrorMessage());
            sysLog.setUserAgent(logEvent.getUserAgent());
            sysLog.setCreateTime(logEvent.getCreateTime());

            // 保存到数据库
            sysLogService.save(sysLog);
            
            log.debug("日志已保存到数据库: {}", sysLog.getOperation());
        } catch (Exception e) {
            log.error("保存日志到数据库失败: {}", e.getMessage(), e);
        }
    }
} 