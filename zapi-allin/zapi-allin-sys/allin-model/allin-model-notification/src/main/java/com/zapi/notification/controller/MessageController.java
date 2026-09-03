package com.zapi.notification.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.notification.entity.Message;
import com.zapi.notification.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息Controller
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/notification/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息
     */
    @ApiOperation("发送消息")
    @PostMapping("/send")
    public Result<Boolean> sendMessage(@RequestBody Message message) {
        try {
            if (message.getReceiverTargets() == null || message.getReceiverTargets().isEmpty()) {
                return Result.error("接收者不能为空");
            }
            boolean success = messageService.sendMessage(message);
            return success ? Result.success("消息发送成功", true) : Result.error("消息发送失败");
        } catch (Exception e) {
            return Result.error("消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送消息
     */
    @ApiOperation("批量发送消息")
    @PostMapping("/send/batch")
    public Result<Integer> sendBatchMessages(@RequestBody List<Message> messages) {
        try {
            int count = messageService.sendBatchMessages(messages);
            return Result.success("成功发送 " + count + " 条消息", count);
        } catch (Exception e) {
            return Result.error("批量发送失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询接收的消息
     */
    @ApiOperation("分页查询接收的消息")
    @GetMapping("/received")
    public Result<Page<Message>> getReceivedMessages(
            @ApiParam("接收者ID") @RequestParam Long receiverId,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("消息状态(UNREAD/READ/DELETED)") @RequestParam(required = false) String status) {
        
        Page<Message> page = messageService.getMessagesByReceiverId(receiverId, current, size);

        // 内存过滤（receiver_id 已移除，主查询已基于关联表）
        if (status != null && page.getRecords() != null) {
            java.util.List<Message> filtered = page.getRecords().stream()
                    .filter(m -> status == null || status.equalsIgnoreCase(m.getReceiverStatus()))
                    .collect(java.util.stream.Collectors.toList());
            page.setRecords(filtered);
            page.setTotal(filtered.size());
        }

        return Result.success(page);
    }

    /**
     * 分页查询发送的消息
     */
    @ApiOperation("分页查询发送的消息")
    @GetMapping("/sent")
    public Result<Page<Message>> getSentMessages(
            @ApiParam("发送者ID") @RequestParam Long senderId,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size) {
        
        Page<Message> page = messageService.getMessagesBySenderId(senderId, current, size);
        return Result.success(page);
    }

    /**
     * 标记消息为已读
     */
    @ApiOperation("标记消息为已读")
    @PutMapping("/read/{messageId}")
    public Result<Boolean> markAsRead(
            @ApiParam("消息ID") @PathVariable Long messageId,
            @ApiParam("接收者ID") @RequestParam Long receiverId) {
        
        try {
            boolean success = messageService.markAsRead(messageId, receiverId);
            return success ? Result.success("标记成功", true) : Result.error("标记失败");
        } catch (Exception e) {
            return Result.error("标记失败: " + e.getMessage());
        }
    }

    /**
     * 批量标记消息为已读
     */
    @ApiOperation("批量标记消息为已读")
    @PutMapping("/read/batch")
    public Result<Integer> markBatchAsRead(
            @ApiParam("消息ID列表") @RequestBody List<Long> messageIds,
            @ApiParam("接收者ID") @RequestParam Long receiverId) {
        
        try {
            int count = messageService.markBatchAsRead(messageIds, receiverId);
            return Result.success("成功标记 " + count + " 条消息", count);
        } catch (Exception e) {
            return Result.error("批量标记失败: " + e.getMessage());
        }
    }

    /**
     * 获取未读消息数量
     */
    @ApiOperation("获取未读消息数量")
    @GetMapping("/unread/count")
    public Result<Integer> getUnreadCount(@ApiParam("接收者ID") @RequestParam Long receiverId) {
        int count = messageService.getUnreadCount(receiverId);
        return Result.success(count);
    }

    /**
     * 删除消息
     */
    @ApiOperation("删除消息")
    @DeleteMapping("/{messageId}")
    public Result<Boolean> deleteMessage(
            @ApiParam("消息ID") @PathVariable Long messageId,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        try {
            boolean success = messageService.deleteMessage(messageId, userId);
            return success ? Result.success("删除成功", true) : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除消息
     */
    @ApiOperation("批量删除消息")
    @DeleteMapping("/batch")
    public Result<Integer> deleteBatchMessages(
            @ApiParam("消息ID列表") @RequestBody List<Long> messageIds,
            @ApiParam("用户ID") @RequestParam Long userId) {
        
        try {
            int count = messageService.deleteBatchMessages(messageIds, userId);
            return Result.success("成功删除 " + count + " 条消息", count);
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取消息详情
     */
    @ApiOperation("获取消息详情")
    @GetMapping("/{messageId}")
    public Result<Message> getMessage(@ApiParam("消息ID") @PathVariable Long messageId) {
        Message message = messageService.getById(messageId);
        if (message != null) {
            return Result.success(message);
        }
        return Result.error("消息不存在");
    }
} 