package com.zapi.file.factory;

import com.zapi.file.config.FileConfig;
import com.zapi.file.strategy.FileStorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件存储工厂类
 * 根据配置选择不同的文件存储策略
 */
@Slf4j
@Component
public class FileStorageFactory {

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    @Qualifier("localFileStorageStrategy")
    private FileStorageStrategy localFileStorageStrategy;

    // 存储策略映射
    private Map<String, FileStorageStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        // 注册存储策略
        strategyMap.put("LOCAL", localFileStorageStrategy);
        // 后续可以添加OSS、FTP等策略
        // strategyMap.put("OSS", ossFileStorageStrategy);
        // strategyMap.put("FTP", ftpFileStorageStrategy);

        log.info("文件存储工厂初始化完成，当前支持的存储类型: {}", strategyMap.keySet());
    }

    /**
     * 获取文件存储策略
     *
     * @return 文件存储策略
     */
    public FileStorageStrategy getStorageStrategy() {
        String storageType = fileConfig.getStorageType();
        FileStorageStrategy strategy = strategyMap.get(storageType);

        if (strategy == null) {
            log.error("未找到存储类型为 {} 的策略，使用默认本地存储", storageType);
            strategy = localFileStorageStrategy;
        }

        return strategy;
    }

    /**
     * 根据存储类型获取文件存储策略
     *
     * @param storageType 存储类型
     * @return 文件存储策略
     */
    public FileStorageStrategy getStorageStrategy(String storageType) {
        FileStorageStrategy strategy = strategyMap.get(storageType);

        if (strategy == null) {
            log.error("未找到存储类型为 {} 的策略，使用默认本地存储", storageType);
            strategy = localFileStorageStrategy;
        }

        return strategy;
    }

    /**
     * 注册存储策略
     *
     * @param storageType 存储类型
     * @param strategy 存储策略
     */
    public void registerStrategy(String storageType, FileStorageStrategy strategy) {
        strategyMap.put(storageType, strategy);
        log.info("注册存储策略: {} -> {}", storageType, strategy.getClass().getSimpleName());
    }
} 