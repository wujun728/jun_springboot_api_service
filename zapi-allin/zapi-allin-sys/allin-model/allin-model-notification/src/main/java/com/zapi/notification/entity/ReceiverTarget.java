package com.zapi.notification.entity;

import lombok.Data;

/**
 * 接收者目标：部门/角色/用户
 */
@Data
public class ReceiverTarget {
    private Long id;
    /**
     * dept/role/user
     */
    private String type;
}

