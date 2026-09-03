package com.zapi.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.notification.entity.MessageTemplate;
import com.zapi.notification.mapper.MessageTemplateMapper;
import com.zapi.notification.service.MessageTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板Service实现类
 */
@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements MessageTemplateService {

    @Override
    public MessageTemplate getByTemplateCode(String templateCode) {
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_code", templateCode)
                   .eq("status", "ENABLED"); // 只查询启用的模板
        
        return getOne(queryWrapper);
    }

    @Override
    @Transactional
    public boolean enableTemplate(Long templateId) {
        UpdateWrapper<MessageTemplate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", templateId)
                    .set("status", "ENABLED");
        
        return update(updateWrapper);
    }

    @Override
    @Transactional
    public boolean disableTemplate(Long templateId) {
        UpdateWrapper<MessageTemplate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", templateId)
                    .set("status", "DISABLED");
        
        return update(updateWrapper);
    }

    @Override
    public String renderTemplate(String templateCode, Map<String, Object> params) {
        MessageTemplate template = getByTemplateCode(templateCode);
        if (template == null) {
            throw new RuntimeException("模板不存在或已禁用: " + templateCode);
        }
        
        String content = template.getTemplateContent();
        if (params != null && !params.isEmpty()) {
            content = replacePlaceholders(content, params);
        }
        
        return content;
    }

    @Override
    public boolean existsByTemplateCode(String templateCode) {
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_code", templateCode);
        
        return count(queryWrapper) > 0;
    }

    /**
     * 替换模板中的占位符
     * @param content 模板内容
     * @param params 参数
     * @return 替换后的内容
     */
    private String replacePlaceholders(String content, Map<String, Object> params) {
        if (!StringUtils.hasText(content) || params == null || params.isEmpty()) {
            return content;
        }
        
        // 使用正则表达式匹配 ${paramName} 格式的占位符
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String paramName = matcher.group(1);
            Object paramValue = params.get(paramName);
            String replacement = paramValue != null ? paramValue.toString() : "";
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
} 