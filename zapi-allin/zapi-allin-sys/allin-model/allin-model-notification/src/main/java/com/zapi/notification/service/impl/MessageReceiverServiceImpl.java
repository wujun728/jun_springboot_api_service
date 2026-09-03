package com.zapi.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.notification.entity.MessageReceiver;
import com.zapi.notification.mapper.MessageReceiverMapper;
import com.zapi.notification.service.MessageReceiverService;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiverServiceImpl extends ServiceImpl<MessageReceiverMapper, MessageReceiver> implements MessageReceiverService {
}

