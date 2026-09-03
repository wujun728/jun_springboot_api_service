package com.zapi.openapi.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据同步策略工厂
 */
@Component
public class DataSyncStrategyFactory {
    
    @Autowired
    private List<DataSyncStrategy> strategies;
    
    /**
     * 策略缓存：key为数据类型
     */
    private final Map<String, DataSyncStrategy> strategyMap = new ConcurrentHashMap<>();
    
    /**
     * 初始化策略映射
     */
    @PostConstruct
    public void init() {
        for (DataSyncStrategy strategy : strategies) {
            strategyMap.put(strategy.getDataType(), strategy);
        }
    }
    
    /**
     * 根据数据类型获取策略
     *
     * @param dataType 数据类型（user/role/dept）
     * @return 数据同步策略
     */
    public DataSyncStrategy getStrategy(String dataType) {
        DataSyncStrategy strategy = strategyMap.get(dataType);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的数据类型: " + dataType);
        }
        return strategy;
    }
}