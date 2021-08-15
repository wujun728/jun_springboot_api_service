package com.chen.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Chenzedeng
 * @Email: chenzedeng@geexfinance.com
 * @Date: 2021/4/20 16:51
 * @description:
 * @version: 1.0
 */
@Data
@NoArgsConstructor
public class DatasourceProperties {

    /**
     * IP
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 数据库账号
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 导出的文件绝对路径
     */
    private String exportPath;
}
