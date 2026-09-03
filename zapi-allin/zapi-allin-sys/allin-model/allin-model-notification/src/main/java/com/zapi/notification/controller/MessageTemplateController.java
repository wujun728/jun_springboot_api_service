package com.zapi.notification.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.notification.entity.MessageTemplate;
import com.zapi.notification.service.MessageTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息模板Controller
 */
@Api(tags = "消息模板管理")
@RestController
@RequestMapping("/notification/message-template")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     * 分页查询消息模板
     */
    @ApiOperation("分页查询消息模板")
    @GetMapping("/list")
    public Result<Page<MessageTemplate>> getTemplateList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("模板名称") @RequestParam(required = false) String templateName,
            @ApiParam("模板状态(ENABLED/DISABLED)") @RequestParam(required = false) String status) {
        
        Page<MessageTemplate> page = new Page<>(current, size);
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        
        if (templateName != null && !templateName.isEmpty()) {
            queryWrapper.like("template_name", templateName);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        Page<MessageTemplate> result = messageTemplateService.page(page, queryWrapper);
        
        return Result.success(result);
    }

    /**
     * 获取所有消息模板
     */
    @ApiOperation("获取所有消息模板")
    @GetMapping("/all")
    public Result<List<MessageTemplate>> getAllTemplates() {
        
        QueryWrapper<MessageTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<MessageTemplate> templates = messageTemplateService.list(queryWrapper);
        
        return Result.success(templates);
    }

    /**
     * 根据ID获取消息模板
     */
    @ApiOperation("根据ID获取消息模板")
    @GetMapping("/{templateId}")
    public Result<MessageTemplate> getTemplate(@ApiParam("模板ID") @PathVariable Long templateId) {
        MessageTemplate template = messageTemplateService.getById(templateId);
        if (template != null) {
            return Result.success(template);
        }
        return Result.error("模板不存在");
    }

    /**
     * 根据编码获取消息模板
     */
    @ApiOperation("根据编码获取消息模板")
    @GetMapping("/code/{templateCode}")
    public Result<MessageTemplate> getTemplateByCode(@ApiParam("模板编码") @PathVariable String templateCode) {
        MessageTemplate template = messageTemplateService.getByTemplateCode(templateCode);
        if (template != null) {
            return Result.success(template);
        }
        return Result.error("模板不存在或已禁用");
    }

    /**
     * 添加消息模板
     */
    @ApiOperation("添加消息模板")
    @PostMapping("/add")
    public Result<Boolean> addTemplate(@RequestBody MessageTemplate template) {
        try {
            // 检查模板编码是否已存在
            if (messageTemplateService.existsByTemplateCode(template.getTemplateCode())) {
                return Result.error("模板编码已存在");
            }
            
            normalizeStatus(template);
            boolean success = messageTemplateService.save(template);
            return success ? Result.success("添加成功", true) : Result.error("添加失败");
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    /**
     * 更新消息模板
     */
    @ApiOperation("更新消息模板")
    @PutMapping("/update")
    public Result<Boolean> updateTemplate(@RequestBody MessageTemplate template) {
        try {
            // 检查模板是否存在
            MessageTemplate existingTemplate = messageTemplateService.getById(template.getId());
            if (existingTemplate == null) {
                return Result.error("模板不存在");
            }
            
            // 检查模板编码是否重复（排除自己）
            if (!existingTemplate.getTemplateCode().equals(template.getTemplateCode())) {
                if (messageTemplateService.existsByTemplateCode(template.getTemplateCode())) {
                    return Result.error("模板编码已存在");
                }
            }
            
            normalizeStatus(template);
            boolean success = messageTemplateService.updateById(template);
            return success ? Result.success("更新成功", true) : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除消息模板
     */
    @ApiOperation("删除消息模板")
    @DeleteMapping("/{templateId}")
    public Result<Boolean> deleteTemplate(@ApiParam("模板ID") @PathVariable Long templateId) {
        try {
            boolean success = messageTemplateService.removeById(templateId);
            return success ? Result.success("删除成功", true) : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 启用消息模板
     */
    @ApiOperation("启用消息模板")
    @PutMapping("/enable/{templateId}")
    public Result<Boolean> enableTemplate(@ApiParam("模板ID") @PathVariable Long templateId) {
        try {
            boolean success = messageTemplateService.enableTemplate(templateId);
            return success ? Result.success("启用成功", true) : Result.error("启用失败");
        } catch (Exception e) {
            return Result.error("启用失败: " + e.getMessage());
        }
    }

    /**
     * 禁用消息模板
     */
    @ApiOperation("禁用消息模板")
    @PutMapping("/disable/{templateId}")
    public Result<Boolean> disableTemplate(@ApiParam("模板ID") @PathVariable Long templateId) {
        try {
            boolean success = messageTemplateService.disableTemplate(templateId);
            return success ? Result.success("禁用成功", true) : Result.error("禁用失败");
        } catch (Exception e) {
            return Result.error("禁用失败: " + e.getMessage());
        }
    }

    /**
     * 渲染模板内容
     */
    @ApiOperation("渲染模板内容")
    @PostMapping("/render/{templateCode}")
    public Result<String> renderTemplate(
            @ApiParam("模板编码") @PathVariable String templateCode,
            @ApiParam("模板参数") @RequestBody Map<String, Object> params) {
        
        try {
            String content = messageTemplateService.renderTemplate(templateCode, params);
            return Result.success(content);
        } catch (Exception e) {
            return Result.error("渲染失败: " + e.getMessage());
        }
    }

    /**
     * 兼容前端传入的状态（0/1 或 ENABLED/DISABLED）
     */
    private void normalizeStatus(MessageTemplate template) {
        if (template == null) {
            return;
        }
        String status = template.getStatus();
        if (!org.springframework.util.StringUtils.hasText(status)) {
            template.setStatus("ENABLED");
            return;
        }
        if ("1".equals(status) || "ENABLED".equalsIgnoreCase(status)) {
            template.setStatus("ENABLED");
        } else if ("0".equals(status) || "DISABLED".equalsIgnoreCase(status)) {
            template.setStatus("DISABLED");
        }
    }
} 