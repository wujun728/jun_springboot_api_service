package org.harmony.toddler.mybatis.groovy.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：配置信息 实体
 *
 * @author diva
 * @since 2021/06/06 08:48
 * @version V1.0
 */
@Data
@ConfigurationProperties(prefix = "mybatis.groovy")
public class MybatisGroovyLangDriverProperties {

    private String[] roots;

    private String baseClass;

}