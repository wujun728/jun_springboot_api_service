package com.zapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.SysConfig;

/**
 * 参数配置Service接口
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 根据参数键名查询参数配置
     *
     * @param configKey 参数键名
     * @return 参数配置
     */
    SysConfig selectConfigByKey(String configKey);

    /**
     * 根据参数键名获取参数键值
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigValueByKey(String configKey);

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置
     * @return 结果 true-唯一 false-不唯一
     */
    boolean checkConfigKeyUnique(SysConfig config);
}