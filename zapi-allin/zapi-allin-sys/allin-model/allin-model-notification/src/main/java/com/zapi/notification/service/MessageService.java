package com.zapi.notification.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.notification.entity.Message;

import java.util.List;

/**
 * 消息Service接口
 */
public interface MessageService extends IService<Message> {

    /**
     * 发送消息
     * @param message 消息对象
     * @return 是否发送成功
     */
    boolean sendMessage(Message message);

    /**
     * 批量发送消息
     * @param messages 消息列表
     * @return 发送成功的消息数量
     */
    int sendBatchMessages(List<Message> messages);

    /**
     * 根据接收者ID查询消息
     * @param receiverId 接收者ID
     * @param current 当前页
     * @param size 每页大小
     * @return 消息分页结果
     */
    Page<Message> getMessagesByReceiverId(Long receiverId, Integer current, Integer size);

    /**
     * 根据发送者ID查询消息
     * @param senderId 发送者ID
     * @param current 当前页
     * @param size 每页大小
     * @return 消息分页结果
     */
    Page<Message> getMessagesBySenderId(Long senderId, Integer current, Integer size);

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param receiverId 接收者ID
     * @return 是否标记成功
     */
    boolean markAsRead(Long messageId, Long receiverId);

    /**
     * 批量标记消息为已读
     * @param messageIds 消息ID列表
     * @param receiverId 接收者ID
     * @return 标记成功的消息数量
     */
    int markBatchAsRead(List<Long> messageIds, Long receiverId);

    /**
     * 获取用户未读消息数量
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    int getUnreadCount(Long receiverId);

    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteMessage(Long messageId, Long userId);

    /**
     * 批量删除消息
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 删除成功的消息数量
     */
    int deleteBatchMessages(List<Long> messageIds, Long userId);
} 