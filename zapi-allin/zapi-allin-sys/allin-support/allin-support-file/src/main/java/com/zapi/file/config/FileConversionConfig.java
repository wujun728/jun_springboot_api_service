package com.zapi.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件转换配置
 * 配置文件转换相关的参数
 */
@Configuration
@ConfigurationProperties(prefix = "zapi.file.conversion")
public class FileConversionConfig {

    /**
     * 是否启用文件转换功能
     */
    private boolean enabled = true;

    /**
     * 转换临时目录
     */
    private String tempDir = System.getProperty("java.io.tmpdir");

    /**
     * 最大文件大小（MB）
     */
    private long maxFileSize = 100;

    /**
     * 转换超时时间（秒）
     */
    private int timeout = 300;

    /**
     * 是否启用异步转换
     */
    private boolean asyncConversion = false;

    /**
     * 转换任务保留时间（小时）
     */
    private int taskRetentionHours = 24;

    /**
     * 支持的输入格式
     */
    private String[] supportedInputFormats = {"doc", "docx", "pdf", "ofd"};

    /**
     * 支持的输出格式
     */
    private String[] supportedOutputFormats = {"pdf", "docx", "ofd"};

    // Getter和Setter方法
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isAsyncConversion() {
        return asyncConversion;
    }

    public void setAsyncConversion(boolean asyncConversion) {
        this.asyncConversion = asyncConversion;
    }

    public int getTaskRetentionHours() {
        return taskRetentionHours;
    }

    public void setTaskRetentionHours(int taskRetentionHours) {
        this.taskRetentionHours = taskRetentionHours;
    }

    public String[] getSupportedInputFormats() {
        return supportedInputFormats;
    }

    public void setSupportedInputFormats(String[] supportedInputFormats) {
        this.supportedInputFormats = supportedInputFormats;
    }

    public String[] getSupportedOutputFormats() {
        return supportedOutputFormats;
    }

    public void setSupportedOutputFormats(String[] supportedOutputFormats) {
        this.supportedOutputFormats = supportedOutputFormats;
    }
}
