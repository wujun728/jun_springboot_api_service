package com.ruoyi.api.openapi.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class ApiAuditInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ApiAuditInterceptor.class);

    @Around("@annotation(com.ruoyi.api.openapi.audit.Auditable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        boolean success = true;
        String errorMsg = null;
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            success = false;
            errorMsg = e.getMessage();
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            ApiAuditLog auditLog = new ApiAuditLog();
            auditLog.setCreateTime(new Date());
            auditLog.setStatus(success ? 1 : 0);
            auditLog.setErrorMsg(errorMsg);
            auditLog.setCostTime(cost);
            log.info("API audit: {} {} cost={}ms success={}",
                    auditLog.getApiPath(), auditLog.getMethod(), cost, success);
        }
    }
}