package org.dromara.surpass.persistence.service;

import java.util.Map;

import org.dromara.surpass.entity.ApiRequestUri;

/**
 * API 执行服务
 */
public interface ApiExecuteService{

    /**
     * 执行 API 请求
     *
     * @param apiRequestUri 请求路径信息
     * @param method        请求方法
     * @param params        参数
     * @param isTest        是否测试
     * @return 执行结果
     */
    Object execute(ApiRequestUri apiRequestUri, String method, Map<String, Object> params, boolean isTest);

}
