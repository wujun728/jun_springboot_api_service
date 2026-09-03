package com.zapi.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息通知配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "zapi.notification")
public class NotificationConfig {

    /**
     * 站内信配置
     */
    private StationMessage stationMessage = new StationMessage();

    /**
     * 实时消息配置
     */
    private RealTimeMessage realTimeMessage = new RealTimeMessage();

    /**
     * 短信配置
     */
    private Sms sms = new Sms();

    /**
     * 邮件配置
     */
    private Email email = new Email();

    /**
     * 站内信配置
     */
    @Data
    public static class StationMessage {
        /**
         * 是否启用站内信
         */
        private boolean enabled = true;

        /**
         * 消息保留天数
         */
        private Integer retentionDays = 90;

        /**
         * 最大消息长度
         */
        private Integer maxContentLength = 1000;
    }

    /**
     * 实时消息配置
     */
    @Data
    public static class RealTimeMessage {
        /**
         * 是否启用实时消息
         */
        private boolean enabled = true;

        /**
         * WebSocket端点
         */
        private String endpoint = "/ws/notification";

        /**
         * 心跳间隔（秒）
         */
        private Integer heartbeatInterval = 30;

        /**
         * 连接超时时间（秒）
         */
        private Integer connectionTimeout = 60;
    }

    /**
     * 短信配置
     */
    @Data
    public static class Sms {
        /**
         * 是否启用短信
         */
        private boolean enabled = false;

        /**
         * 短信服务商（ALIYUN-阿里云，TENCENT-腾讯云）
         */
        private String provider = "ALIYUN";

        /**
         * 阿里云短信配置
         */
        private AliyunSms aliyun = new AliyunSms();

        /**
         * 腾讯云短信配置
         */
        private TencentSms tencent = new TencentSms();
    }

    /**
     * 邮件配置
     */
    @Data
    public static class Email {
        /**
         * 是否启用邮件
         */
        private boolean enabled = false;

        /**
         * SMTP服务器地址
         */
        private String host;

        /**
         * SMTP端口
         */
        private Integer port = 587;

        /**
         * 邮箱用户名
         */
        private String username;

        /**
         * 邮箱密码
         */
        private String password;

        /**
         * 发件人邮箱
         */
        private String from;

        /**
         * 发件人名称
         */
        private String fromName;
    }

    /**
     * 阿里云短信配置
     */
    @Data
    public static class AliyunSms {
        /**
         * 访问密钥ID
         */
        private String accessKeyId;

        /**
         * 访问密钥Secret
         */
        private String accessKeySecret;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 短信模板编码
         */
        private String templateCode;
    }

    /**
     * 腾讯云短信配置
     */
    @Data
    public static class TencentSms {
        /**
         * 应用ID
         */
        private String appId;

        /**
         * 应用密钥
         */
        private String appKey;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 短信模板ID
         */
        private String templateId;
    }
} 