package com.zapi.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.notification.entity.MessageTemplate;

import java.util.Map;

/**
 * 消息模板Service接口
 */
public interface MessageTemplateService extends IService<MessageTemplate> {

    /**
     * 根据模板编码获取模板
     * @param templateCode 模板编码
     * @return 消息模板
     */
    MessageTemplate getByTemplateCode(String templateCode);

    /**
     * 启用模板
     * @param templateId 模板ID
     * @return 是否启用成功
     */
    boolean enableTemplate(Long templateId);

    /**
     * 禁用模板
     * @param templateId 模板ID
     * @return 是否禁用成功
     */
    boolean disableTemplate(Long templateId);

    /**
     * 渲染模板内容
     * @param templateCode 模板编码
     * @param params 参数
     * @return 渲染后的内容
     */
    String renderTemplate(String templateCode, Map<String, Object> params);

    /**
     * 检查模板编码是否存在
     * @param templateCode 模板编码
     * @return 是否存在
     */
    boolean existsByTemplateCode(String templateCode);
} 