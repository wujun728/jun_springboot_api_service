package com.zapi.dataset.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

// 使用@Data来自动生成getter/setter方法
@Data
// @TableName用于指定对应的数据库表名，默认是类名转小写下划线格式
@TableName("ds_data_source")
public class DataSourceEntity {

    // 标识主键生成策略为自增
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 数据库类型字段
    private String dbType;

    // 数据库连接URL，确保唯一性
    private String url;

    // 连接数据库使用的用户名
    private String username;

    // 连接数据库使用的密码（建议存储加密后的数据）
    private String password;

    // 数据库Schema名称，可为空
    private String schemaName;

    // 创建时间，默认值为当前时间戳，插入时自动填充
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新时间，默认值为当前时间戳，插入或更新时自动填充
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
