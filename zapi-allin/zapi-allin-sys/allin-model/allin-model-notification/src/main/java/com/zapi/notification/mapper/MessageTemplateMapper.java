package com.zapi.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.notification.entity.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息模板Mapper接口
 */
@Mapper
public interface MessageTemplateMapper extends BaseMapper<MessageTemplate> {
} 