package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.api.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public AjaxResult saveLog(Object operLog, String source) {
                return AjaxResult.error("保存操作日志失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult saveLogininfor(Object logininfor, String source) {
                return AjaxResult.error("保存登录日志失败:" + throwable.getMessage());
            }
        };
    }
}
