package org.harmony.toddler.mybatis.groovy.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.scripting.LanguageDriver;
import org.harmony.toddler.mybatis.groovy.GroovyLangDriver;
import org.harmony.toddler.mybatis.groovy.GroovyLangDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@EnableConfigurationProperties(MybatisGroovyLangDriverProperties.class)
@ConditionalOnClass({LanguageDriver.class, GroovyLangDriver.class, GroovyLangDriverConfig.class})
public class MybatisGroovyLangDriverAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MybatisGroovyLangDriverAutoConfiguration.class);

    private final MybatisGroovyLangDriverProperties properties;

    public MybatisGroovyLangDriverAutoConfiguration(MybatisGroovyLangDriverProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    GroovyLangDriver groovyLangDriver(GroovyLangDriverConfig config) {
        return new GroovyLangDriver(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroovyLangDriverConfig groovyLangDriverConfig() {
        if (properties.getRoots() != null && properties.getRoots().length > 0) {
            GroovyLangDriverConfig.setRoots(properties.getRoots());
        }
        if (StringUtils.isNotEmpty(properties.getBaseClass())) {
            GroovyLangDriverConfig.setBaseClass(properties.getBaseClass());
        }
        return GroovyLangDriverConfig.newInstance();
    }

    /**
     *
     */
    public static class AutoConfiguredTypeAliasRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.debug("Could not determine auto-configuration package, automatic type alias registry disabled.");
                return;
            }

            org.apache.ibatis.session.Configuration configuration = beanFactory.getBean(
                    org.apache.ibatis.session.Configuration.class);
            configuration.getTypeAliasRegistry().registerAlias("groovy", GroovyLangDriver.class);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }
    }

    /**
     *
     */
    @Configuration
    @Import(AutoConfiguredTypeAliasRegistrar.class)
    @ConditionalOnBean({org.apache.ibatis.session.Configuration.class})
    public static class TypeAliasRegistryConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            logger.debug(
                    "Not found configuration for registering mapper bean using @MapperScan, MapperFactoryBean and MapperScannerConfigurer.");
        }
    }
}
