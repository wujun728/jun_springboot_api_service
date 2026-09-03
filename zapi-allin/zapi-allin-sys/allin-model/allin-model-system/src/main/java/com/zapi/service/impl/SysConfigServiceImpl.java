package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.SysConfig;
import com.zapi.mapper.SysConfigMapper;
import com.zapi.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 参数配置Service实现类
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    /**
     * 根据参数键名查询参数配置
     *
     * @param configKey 参数键名
     * @return 参数配置
     */
    @Override
    public SysConfig selectConfigByKey(String configKey) {
        if (!StringUtils.hasText(configKey)) {
            return null;
        }
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigKey, configKey);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据参数键名获取参数键值
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    @Override
    public String selectConfigValueByKey(String configKey) {
        SysConfig config = selectConfigByKey(configKey);
        if (config != null) {
            return config.getConfigValue();
        }
        return null;
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置
     * @return 结果 true-唯一 false-不唯一
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        Integer configId = config.getConfigId() == null ? -1 : config.getConfigId();
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigKey, config.getConfigKey());
        SysConfig info = this.getOne(queryWrapper);
        if (info != null && !info.getConfigId().equals(configId)) {
            return false;
        }
        return true;
    }
}