package com.gitee.freakchicken.dbapi.conf;

import com.gitee.freakchicken.dbapi.util.EncrypDES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EncryptConfig {

    private static final Logger logger = LoggerFactory.getLogger(EncryptConfig.class);

    private static final String DEFAULT_KEY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";

    @Value("${dbapi.encrypt.key:}")
    private String encryptKey;

    @PostConstruct
    public void init() {
        if (encryptKey == null || encryptKey.trim().isEmpty()) {
            logger.warn("未配置 dbapi.encrypt.key，使用默认 AES 密钥（仅限开发环境！）");
            return;
        }
        if (DEFAULT_KEY.equals(encryptKey.trim())) {
            logger.warn("dbapi.encrypt.key 使用的是默认密钥，生产环境请替换！");
        }
        EncrypDES.setKey(encryptKey);
        logger.info("AES-256 加密密钥已从配置加载");
    }
}
