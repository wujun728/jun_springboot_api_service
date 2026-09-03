package com.zapi.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class SpringContextUtil implements ApplicationContextAware, EmbeddedValueResolverAware, BeanFactoryPostProcessor {

    private static ApplicationContext context;
    private static StringValueResolver stringValueResolver;
    private static ConfigurableListableBeanFactory beanFactory;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> Map<String, T> getBeanMap(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static <T> List<T> getBeanList(Class<T> clazz) {
        Map<String, T> map = getBeanMap(clazz);
        return CollectionUtils.isEmpty(map) ? null : map.values().stream().collect(Collectors.toList());
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory = beanFactory;
    }

    public static ListableBeanFactory getBeanFactory() {
        return (ListableBeanFactory)(null == beanFactory ? context : beanFactory);
    }
    /**
     * 获取当前配置属性
     */
    public static String getEnvProperty(String key){
        assert key!=null;
        if (null == context) {
            return null;
        }
        return context.getEnvironment().getProperty(key);
    }


    /**
     * 获取spring value，和@Value的效果相同
     * 注意：如果是获取配置，找不到配置会报异常
     *
     * @param spEL spring表达式，须用"${spEL}"括起来
     * @return 表达式的值
     */
    public static String getSpringValue(String spEL) {
        return stringValueResolver.resolveStringValue(spEL);
    }

    /**
     * 获取当前profile
     */
    public static String getProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 当前环境是否是开发环境
     */
    public static boolean isDev() {
        return "dev".equals(getProfile());
    }

    /**
     * 当前环境是否是测试环境
     */
    public static boolean isTest() {
        return "test".equals(getProfile());
    }

    /**
     * 当前环境是否是生产环境
     */
    public static boolean isProd() {
        return "prod".equals(getProfile());
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.context = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        SpringContextUtil.stringValueResolver = resolver;
    }

    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws RuntimeException {
        final ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else if (context instanceof ConfigurableApplicationContext) {
            factory = ((ConfigurableApplicationContext) context).getBeanFactory();
        } else {
            throw new RuntimeException("No ConfigurableListableBeanFactory from context!");
        }
        return factory;
    }
}
