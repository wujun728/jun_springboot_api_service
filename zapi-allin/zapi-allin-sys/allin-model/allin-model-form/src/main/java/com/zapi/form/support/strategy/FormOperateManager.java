package com.zapi.form.support.strategy;

import com.zapi.enums.FormOperationEnum;
import com.zapi.form.support.strategy.service.FormOperateService;
import com.zapi.form.support.strategy.service.impl.AddTableOperateServiceImpl;
import com.zapi.form.support.strategy.service.impl.UpdateTableOperateServicelmpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略模式---数据库模型相关操作
 * @ClassName FormOperateManager
 * @Author zhonglz~
 * @create 2025年08月20日09:04:47
 */
@Slf4j
@Component
public class FormOperateManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static final Map<FormOperationEnum, Class<? extends FormOperateService>> formOperateServiceClassMap = new ConcurrentHashMap<>();

    static {
        formOperateServiceClassMap.put(FormOperationEnum.ADDTABLE, AddTableOperateServiceImpl.class);
        formOperateServiceClassMap.put(FormOperationEnum.UPDATETABLE, UpdateTableOperateServicelmpl.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FormOperateManager.applicationContext = applicationContext;
    }

    public static FormOperateService formOperateService(FormOperationEnum operationType) {
        log.info("进入策略，操作类型：operationType:{}", operationType.getLabel());
        Class<? extends FormOperateService> serviceClass = formOperateServiceClassMap.get(operationType);
        if (serviceClass != null && applicationContext != null) {
            return applicationContext.getBean(serviceClass);
        }
        log.error("未找到对应的策略服务或ApplicationContext未初始化: {}", operationType);
        return null;
    }
}
