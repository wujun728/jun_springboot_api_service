package com.gitee.freakchicken.dbapi.conf;

import com.gitee.freakchicken.dbapi.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class JwtConfig {

    @Value("${dbapi.jwt.secret}")
    private String jwtSecret;

    private static final int MIN_SECRET_LENGTH = 32;
    private static final String DEFAULT_SECRET_PREFIX = "ChangeMe";

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            throw new IllegalStateException("dbapi.jwt.secret must be configured");
        }
        if (jwtSecret.length() < MIN_SECRET_LENGTH) {
            throw new IllegalStateException("dbapi.jwt.secret must be at least " + MIN_SECRET_LENGTH + " characters");
        }
        if (jwtSecret.startsWith(DEFAULT_SECRET_PREFIX)) {
            log.warn("WARNING: Using default JWT secret! Please change dbapi.jwt.secret in production!");
        }
        JwtUtils.setSecret(jwtSecret);
        log.info("JWT secret initialized from configuration");
    }
}
