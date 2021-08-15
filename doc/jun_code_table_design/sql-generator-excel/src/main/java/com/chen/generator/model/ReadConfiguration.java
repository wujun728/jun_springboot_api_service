package com.chen.generator.model;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * @Author Eddy·Chen
 * @Email 835033913@qq.com
 * @Create 2019-03-12 14:38
 */
public class ReadConfiguration {

    private File root;

    /**
     * 要读取的配置文件名称
     */
    public static final String CON_NAME = "datasource.properties";

    private DatasourceProperties datasourceProperties;

    public ReadConfiguration() {
        String jarLocal = null;
        try {
            jarLocal = new String(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.root = new File(jarLocal).getParentFile();
        System.out.println("---初始化获得路径--" + this.root.getAbsolutePath());

        Props props = new Props(new File(root, CON_NAME), "utf-8");

        datasourceProperties = new DatasourceProperties();
        Field[] fields = ReflectUtil.getFields(DatasourceProperties.class);
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object val = props.get(fieldName);
            if (val == null) {
                throw new RuntimeException(StrUtil.format("字段:{} 未配置请检查配置文件", fieldName));
            }
            ReflectUtil.setFieldValue(datasourceProperties, field, val);
        }
    }

    public DatasourceProperties getDatasourceProperties() {
        return datasourceProperties;
    }
}
