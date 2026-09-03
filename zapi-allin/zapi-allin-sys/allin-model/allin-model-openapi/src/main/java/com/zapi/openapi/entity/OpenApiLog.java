package com.zapi.openapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 接口调用日志实体类
 */
@Data
@TableName("open_api_log")
public class OpenApiLog {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置ID
     */
    private Long configId;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 调用类型：1-被动 2-主动
     */
    private Integer callType;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 状态：0-失败 1-成功
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 同步数据条数
     */
    private Integer syncCount;

    /**
     * 耗时（毫秒）
     */
    private Long costTime;

    /**
     * 调用时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 客户端IP（被动模式）
     */
    private String clientIp;

    /**
     * 关联的配置名称（非数据库字段）
     */
    @TableField(exist = false)
    private String configName;

    /**
     * 关联的应用名称（非数据库字段）
     */
    @TableField(exist = false)
    private String appName;
}
