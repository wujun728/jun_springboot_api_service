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

    @PostConstruct
    public void init() {
        JwtUtils.setSecret(jwtSecret);
        log.info("JWT secret initialized from configuration");
    }
}
