package com.zapi.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.notification.config.NotificationConfig;
import com.zapi.notification.entity.Message;
import com.zapi.notification.entity.MessageReceiver;
import com.zapi.notification.entity.ReceiverTarget;
import com.zapi.notification.mapper.MessageMapper;
import com.zapi.notification.service.MessageReceiverService;
import com.zapi.notification.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息Service实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final MessageReceiverService messageReceiverService;
    private final ObjectMapper objectMapper;
    private final NotificationConfig notificationConfig;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageServiceImpl(MessageReceiverService messageReceiverService,
                              ObjectMapper objectMapper,
                              NotificationConfig notificationConfig,
                              SimpMessagingTemplate messagingTemplate) {
        this.messageReceiverService = messageReceiverService;
        this.objectMapper = objectMapper;
        this.notificationConfig = notificationConfig;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public boolean sendMessage(Message message) {
        if (message.getReceiverTargets() == null || message.getReceiverTargets().isEmpty()) {
            throw new IllegalArgumentException("接收者不能为空");
        }
        serializeTargets(message);
        if (message.getCreateTime() == null) {
            message.setCreateTime(LocalDateTime.now());
        }

        boolean saved = save(message);
        if (!saved) {
            return false;
        }

        // 创建接收人记录
        List<MessageReceiver> receivers = saveReceivers(message.getId(), message.getReceiverTargets());
        pushRealtimeNotifications(message, receivers);
        return true;
    }

    @Override
    @Transactional
    public int sendBatchMessages(List<Message> messages) {
        // 设置默认值
        for (Message message : messages) {
            if (message.getReceiverTargets() == null || message.getReceiverTargets().isEmpty()) {
                continue; // 跳过无接收人的消息
            }
            serializeTargets(message);
            if (message.getCreateTime() == null) {
                message.setCreateTime(LocalDateTime.now());
            }
        }
        
        boolean saved = saveBatch(messages);
        if (!saved) {
            return 0;
        }

        // 批量保存接收人并推送
        for (Message msg : messages) {
            if (msg.getId() != null) {
                List<MessageReceiver> receivers = saveReceivers(msg.getId(), msg.getReceiverTargets());
                pushRealtimeNotifications(msg, receivers);
            }
        }

        return messages.size();
    }

    @Override
    public Page<Message> getMessagesByReceiverId(Long receiverId, Integer current, Integer size) {
        Page<Message> result = new Page<>(current, size);

        // 先查关联表
        Page<MessageReceiver> receiverPage = new Page<>(current, size);
        QueryWrapper<MessageReceiver> recvWrapper = new QueryWrapper<>();
        recvWrapper.eq("receiver_id", receiverId)
                .orderByDesc("create_time");
        messageReceiverService.page(receiverPage, recvWrapper);

        if (receiverPage.getRecords().isEmpty()) {
            return result;
        }

        List<Long> messageIds = receiverPage.getRecords().stream()
                .map(MessageReceiver::getMessageId)
                .distinct()
                .collect(Collectors.toList());
        QueryWrapper<Message> msgWrapper = new QueryWrapper<>();
        msgWrapper.select("id", "title", "content", "sender_id", "url", "icon", "tag", "create_time", "update_time", "deleted");
        msgWrapper.in("id", messageIds).orderByDesc("create_time");
        List<Message> messages = list(msgWrapper);

        // 关联接收状态
        for (Message msg : messages) {
            // 反序列化 receiverTargets
            parseTargets(msg);
            receiverPage.getRecords().stream()
                    .filter(r -> r.getMessageId().equals(msg.getId()) && receiverId.equals(r.getReceiverId()))
                    .findFirst()
                    .ifPresent(r -> {
                        msg.setReceiverStatus(r.getStatus());
                        msg.setReceiverReadTime(r.getReadTime());
                    });
        }

        result.setRecords(messages);
        result.setTotal(receiverPage.getTotal());
        result.setCurrent(receiverPage.getCurrent());
        result.setSize(receiverPage.getSize());
        return result;
    }

    @Override
    public Page<Message> getMessagesBySenderId(Long senderId, Integer current, Integer size) {
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender_id", senderId)
                   .orderByDesc("create_time");
        
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean markAsRead(Long messageId, Long receiverId) {
        UpdateWrapper<MessageReceiver> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("message_id", messageId)
                .eq("receiver_id", receiverId)
                .set("status", "READ")
                .set("read_time", LocalDateTime.now());

        return messageReceiverService.update(updateWrapper);
    }

    @Override
    @Transactional
    public int markBatchAsRead(List<Long> messageIds, Long receiverId) {
        UpdateWrapper<MessageReceiver> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("message_id", messageIds)
                .eq("receiver_id", receiverId)
                .set("status", "READ")
                .set("read_time", LocalDateTime.now());

        return messageReceiverService.update(updateWrapper) ? messageIds.size() : 0;
    }

    @Override
    public int getUnreadCount(Long receiverId) {
        QueryWrapper<MessageReceiver> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", receiverId)
                .eq("status", "UNREAD");
        return (int) messageReceiverService.count(queryWrapper);
    }

    @Override
    @Transactional
    public boolean deleteMessage(Long messageId, Long userId) {
        // 删除关联表中该用户的记录
        QueryWrapper<MessageReceiver> recvWrapper = new QueryWrapper<>();
        recvWrapper.eq("message_id", messageId).eq("receiver_id", userId);
        messageReceiverService.remove(recvWrapper);

        // 若发送者删除，则删除主消息及所有关联
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", messageId)
                   .and(wrapper -> wrapper.eq("sender_id", userId));

        boolean removed = remove(queryWrapper);
        if (removed) {
            QueryWrapper<MessageReceiver> all = new QueryWrapper<>();
            all.eq("message_id", messageId);
            messageReceiverService.remove(all);
        }
        return true;
    }

    @Override
    @Transactional
    public int deleteBatchMessages(List<Long> messageIds, Long userId) {
        QueryWrapper<MessageReceiver> recvWrapper = new QueryWrapper<>();
        recvWrapper.in("message_id", messageIds).eq("receiver_id", userId);
        messageReceiverService.remove(recvWrapper);

        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", messageIds)
                .eq("sender_id", userId);

        boolean removed = remove(queryWrapper);
        if (removed) {
            QueryWrapper<MessageReceiver> all = new QueryWrapper<>();
            all.in("message_id", messageIds);
            messageReceiverService.remove(all);
        }
        return messageIds.size();
    }

    private List<MessageReceiver> saveReceivers(Long messageId, List<ReceiverTarget> targets) {
        if (messageId == null) return Collections.emptyList();
        List<MessageReceiver> records = new java.util.ArrayList<>();
        if (targets != null && !targets.isEmpty()) {
            for (ReceiverTarget t : targets) {
                if (t == null || t.getId() == null) continue;
                MessageReceiver r = new MessageReceiver();
                r.setMessageId(messageId);
                r.setReceiverTargetId(t.getId());
                r.setReceiverType(t.getType());
                if ("user".equalsIgnoreCase(t.getType())) {
                    r.setReceiverId(t.getId());
                }
                r.setStatus("UNREAD");
                r.setCreateTime(LocalDateTime.now());
                r.setUpdateTime(LocalDateTime.now());
                records.add(r);
            }
        }
        if (!records.isEmpty()) {
            messageReceiverService.saveBatch(records);
        }
        return records;
    }

    private void pushRealtimeNotifications(Message message, List<MessageReceiver> receivers) {
        if (message == null || receivers == null || receivers.isEmpty()) {
            return;
        }
        if (notificationConfig == null
                || !notificationConfig.getRealTimeMessage().isEnabled()
                || messagingTemplate == null) {
            return;
        }

        for (MessageReceiver receiver : receivers) {
            if (receiver == null || receiver.getReceiverId() == null) {
                continue;
            }
            Message payload = buildRealtimePayload(message, receiver);
            // 使用 topic + userId，避免依赖 Principal 名称映射
            messagingTemplate.convertAndSend(
                    "/topic/notification/" + receiver.getReceiverId(),
                    payload
            );
        }
    }

    private Message buildRealtimePayload(Message source, MessageReceiver receiver) {
        Message payload = new Message();
        payload.setId(source.getId());
        payload.setTitle(source.getTitle());
        payload.setContent(source.getContent());
        payload.setMessageUrl(source.getMessageUrl());
        payload.setMessageIcon(source.getMessageIcon());
        payload.setMessageTag(source.getMessageTag());
        payload.setSenderId(source.getSenderId());
        payload.setCreateTime(source.getCreateTime());
        payload.setUpdateTime(source.getUpdateTime());
        payload.setReceiverStatus(receiver != null ? receiver.getStatus() : "UNREAD");
        payload.setReceiverReadTime(receiver != null ? receiver.getReadTime() : null);
        return payload;
    }

    private void serializeTargets(Message message) {
        try {
            if (message.getReceiverTargets() != null) {
                message.setReceiverTargetsJson(objectMapper.writeValueAsString(message.getReceiverTargets()));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("接收者序列化失败: " + e.getMessage(), e);
        }
    }

    private void parseTargets(Message message) {
        if (message == null || message.getReceiverTargetsJson() == null) return;
        try {
            List<ReceiverTarget> list = objectMapper.readValue(
                    message.getReceiverTargetsJson(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ReceiverTarget.class)
            );
            message.setReceiverTargets(list);
        } catch (Exception ignored) {
        }
    }
} 