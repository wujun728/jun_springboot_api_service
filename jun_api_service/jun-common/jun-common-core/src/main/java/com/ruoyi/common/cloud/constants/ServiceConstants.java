package com.ruoyi.common.cloud.constants;

/**
 * 服务名称常量
 *
 * @author ruoyi
 */
public class ServiceConstants {

    /** 单体模式标识 */
    public static final String SERVER_BOOT = "boot";

    /** 认证服务 */
    public static final String AUTH_SERVICE = "jun-auth";

    /** 系统服务 */
    public static final String SYSTEM_SERVICE = "jun-system";

    /** 网关服务 */
    public static final String GATEWAY_SERVICE = "jun-gateway";

    /** 定时任务服务 */
    public static final String JOB_SERVICE = "jun-job";

    /** 代码生成服务 */
    public static final String GEN_SERVICE = "jun-gen";

    /**
     * 判断是否为单体模式
     */
    public static boolean isBoot(String type) {
        return SERVER_BOOT.equals(type);
    }
}
