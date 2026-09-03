package com.zapi.form.annotation;
import com.zapi.form.model.dto.FormTableColumnDTO;
import com.zapi.form.model.entity.Form;
import com.zapi.form.support.request.FormOperationRequest;
import com.zapi.form.support.strategy.FormOperateManager;
import com.zapi.form.support.strategy.service.FormOperateService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * SyncDataPlus注解切面
 * @author zhonglz
 * @date 2025年08月21日14:56:50
 */
@Slf4j
@Aspect
@Component
public class DataBaseOperatePlusAspect {

    @Around("@annotation(com.zapi.form.annotation.DataBaseOperatePlus)")
    public Object handleSyncDataPlus(ProceedingJoinPoint joinPoint) throws Throwable {
        // 先执行原方法
        Object result = joinPoint.proceed();
        // 异步处理同步逻辑，不影响主业务流程
        CompletableFuture.runAsync(() -> {
            try {
                handleDataAsync(joinPoint);
            } catch (Exception e) {
                log.error("异步处理数据库操作失败: {}", e.getMessage(), e);
            }
        });
        return result;
    }

    private void handleDataAsync(ProceedingJoinPoint joinPoint) {
        // 获取注解参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DataBaseOperatePlus dataBaseOperatePlus = method.getAnnotation(DataBaseOperatePlus.class);
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        // 通过类型匹配获取目标实体
        Object targetEntity = getEntityByType(args, dataBaseOperatePlus.targetEntityType());
        // 处理获取到的实体
        if (targetEntity != null) {
            log.info("开始处理数据库操作, 操作类型: {}, 实体类型: {}", 
                    dataBaseOperatePlus.operationType(), targetEntity.getClass().getSimpleName());
            processEntity(targetEntity, dataBaseOperatePlus);
        } else {
            log.warn("未找到目标实体, 目标类型: {}", dataBaseOperatePlus.targetEntityType());
        }
    }
    /**
     * 处理实体的数据库操作
     */
    private void processEntity(Object entity, DataBaseOperatePlus annotation) {
        if (annotation == null) {
            return;
        }
        // 根据实体类型进行不同处理
        if (entity instanceof Form) {
            processFlwFormTemplate((Form) entity, annotation);
        } else {
            log.warn("未知的实体类型: {}", entity.getClass().getName());
        }
    }

    /**
     * 处理 FlwFormTemplate 的数据库操作
     */
    private void processFlwFormTemplate(Form formInfo, DataBaseOperatePlus annotation) {
        if (annotation == null) {
            return;
        }
        try {
            log.info("开始处理表单模板: formId={}, formName={}, 操作类型={}", 
                    formInfo.getId(), formInfo.getFormName(), annotation.operationType());
            
            FormTableColumnDTO tableColumnDTO = new FormTableColumnDTO(formInfo);
            log.info("表单字段解析结果: 字段数量={}", 
                    tableColumnDTO.getColumnInfoDTOList() != null ? tableColumnDTO.getColumnInfoDTOList().size() : 0);
            
            /// 写一个策略,分别调用对应的 新增修改删除表及字段操作
            FormOperateService dataBaseOperateService = FormOperateManager.formOperateService(annotation.operationType());
            dataBaseOperateService.executeDataBaseOperate(FormOperationRequest.builder()
                    .operationType(annotation.operationType())
                    .tableColumnDTO(tableColumnDTO)
                    .build());
            
            log.info("表单模板处理完成: formId={}", formInfo.getId());
        } catch (Exception e) {
            log.error("处理表单模板失败: formId={}, error={}", formInfo.getId(), e.getMessage(), e);
        }
    }

    /**
     * 通过参数类型获取实体
     */
    private Object getEntityByType(Object[] args, Class<?> targetType) {
        for (Object arg : args) {
            if (arg != null && targetType.isAssignableFrom(arg.getClass())) {
                return arg;
            }
        }
        return null;
    }


} 