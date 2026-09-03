package com.ruoyi.system.api;

import com.ruoyi.common.cloud.constants.ServiceConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 日志服务远程调用
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteLogService", value = ServiceConstants.SYSTEM_SERVICE,
        fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {

    /**
     * 保存系统日志
     */
    @PostMapping("/monitor/operlog/add")
    AjaxResult saveLog(@RequestBody Object operLog,
                       @RequestHeader(value = "from-source", required = false) String source);

    /**
     * 保存访问记录
     */
    @PostMapping("/monitor/logininfor/add")
    AjaxResult saveLogininfor(@RequestBody Object logininfor,
                              @RequestHeader(value = "from-source", required = false) String source);
}
