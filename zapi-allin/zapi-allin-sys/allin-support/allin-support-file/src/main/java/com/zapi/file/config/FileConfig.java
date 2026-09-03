package com.zapi.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件服务配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "zapi.file")
public class FileConfig {

    /**
     * 存储类型（LOCAL-本地，OSS-阿里云OSS，FTP-FTP服务器）
     */
    private String storageType = "LOCAL";

    /**
     * 本地存储配置
     */
    private Local local = new Local();

    /**
     * 阿里云OSS配置
     */
    private Oss oss = new Oss();

    /**
     * FTP配置
     */
    private Ftp ftp = new Ftp();

    /**
     * 文件上传配置
     */
    private Upload upload = new Upload();

    /**
     * 本地存储配置
     */
    @Data
    public static class Local {
        /**
         * 本地存储根路径
         */
        private String rootPath = "/Users/zhonglz/work";

        /**
         * 文件访问URL前缀
         */
        private String urlPrefix = "/file/download/";
    }

    /**
     * 阿里云OSS配置
     */
    @Data
    public static class Oss {
        /**
         * 访问密钥ID
         */
        private String accessKeyId;

        /**
         * 访问密钥Secret
         */
        private String accessKeySecret;

        /**
         * 端点
         */
        private String endpoint;

        /**
         * 存储桶名称
         */
        private String bucketName;

        /**
         * 文件访问URL前缀
         */
        private String urlPrefix;
    }

    /**
     * FTP配置
     */
    @Data
    public static class Ftp {
        /**
         * FTP服务器地址
         */
        private String host;

        /**
         * FTP端口
         */
        private Integer port = 21;

        /**
         * FTP用户名
         */
        private String username;

        /**
         * FTP密码
         */
        private String password;

        /**
         * FTP根路径
         */
        private String rootPath = "/";

        /**
         * 文件访问URL前缀
         */
        private String urlPrefix;
    }

    /**
     * 文件上传配置
     */
    @Data
    public static class Upload {
        /**
         * 最大文件大小（MB）
         */
        private Long maxFileSize = 100L;

        /**
         * 允许的文件类型
         */
        private String[] allowedTypes = {
            "jpg", "jpeg", "png", "gif", "bmp", "webp",
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf",
            "txt", "zip", "rar", "7z", "tar", "gz"
        };

        /**
         * 禁止的文件类型
         */
        private String[] forbiddenTypes = {
            "exe", "bat", "cmd", "com", "scr", "pif", "vbs", "js"
        };
    }
} 