package com.ruoyi.api.groovy.engine;

import cn.hutool.core.lang.Console;
import cn.hutool.log.StaticLog;
import com.ruoyi.api.groovy.entity.GroovyApiConfig;
import com.ruoyi.api.groovy.service.GroovyApiService;
import com.ruoyi.api.groovy.cache.GroovyApiConfigCache;
import com.ruoyi.api.groovy.mapping.RequestMappingService;
import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Configuration
@Service
public class GroovyDynamicLoader implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(GroovyDynamicLoader.class);

	private Map<String,Class> classMap = new HashMap<>();

	private ConfigurableApplicationContext applicationContext;

	private BeanDefinitionRegistry registry;

	private static final GroovyClassLoader groovyClassLoader = new GroovyClassLoader(
			GroovyDynamicLoader.class.getClassLoader());

	@Resource
	private GroovyApiService groovyApiService;

	@Resource
	private RequestMappingService requestMappingService;

	public static final String main = "main";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public void initNew() {
        long start = System.currentTimeMillis();
        System.out.println("开始解析groovy脚本...");
		try {
			groovyApiService.init();

			List<GroovyApiConfig> groovyScripts = groovyApiService.queryApiConfigList();

			GroovyApiConfigCache.putAll(groovyScripts);


			initNew(groovyScripts);

			refreshMapping(groovyScripts);
		} catch (IllegalArgumentException e) {
			Console.log("数据源配置有误："+e.getMessage());
			if(e.getMessage().contains("Config not found by configName")) {
				Console.error("数据源配置有误："+e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        long cost = System.currentTimeMillis() - start;
        System.out.println("结束解析groovy脚本...，耗时：" + cost);
	}

	private void initNew(List<GroovyApiConfig> groovyInfos) {
		if (CollectionUtils.isEmpty(groovyInfos)) {
			return;
		}
		for (GroovyApiConfig groovyInfo : groovyInfos) {
			Boolean isSucess = true;
			try {
				if(groovyInfo.getScriptType().equalsIgnoreCase("Class")){
					Class clazz = groovyClassLoader.parseClass(groovyInfo.getScriptContent());
					String clazzname = clazz.getName();
					StaticLog.info("clazzName = "+ clazzname);
					registerBean(clazzname+groovyInfo.getId(), clazz);
				}else if(groovyInfo.getScriptType().equalsIgnoreCase("SQL")){
					log.info("当前Groovy脚本类型SQL类型脚本1：className-{},path-{},beanName-{},BeanType-{}：",groovyInfo.getBeanName(),groovyInfo.getPath(),groovyInfo.getBeanName(),groovyInfo.getScriptType());
				}else {
					log.error("当前Groovy脚本类型不支持1：className-{},path-{},beanName-{},BeanType-{}：",groovyInfo.getBeanName(),groovyInfo.getPath(),groovyInfo.getBeanName(),groovyInfo.getScriptType());
				}
			} catch (BeanDefinitionStoreException e) {
				isSucess = false;
				StaticLog.error(e.getMessage());
			} catch (CompilationFailedException e) {
				isSucess = false;
				StaticLog.error(e.getMessage());
			}catch (Exception e) {
				isSucess = false;
				StaticLog.error(e.getMessage());
			}
			if(!isSucess){
				log.info("当前groovyInfo加载成功1,className-{},path-{},beanName-{},BeanType-{}：",groovyInfo.getBeanName(),groovyInfo.getPath(),groovyInfo.getBeanName(),groovyInfo.getScriptType());
			}
		}
		GroovyApiConfigCache.put2map(groovyInfos);
	}

	public void registerBean(String beanName, Class clazz) {
		this.registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		BeanDefinition beanDefinition = builder.getBeanDefinition();
		registry.registerBeanDefinition(beanName, beanDefinition);
	}


	public void refreshNew() {

		List<GroovyApiConfig> groovyInfos = groovyApiService.queryApiConfigList();

		GroovyApiConfigCache.putAll(groovyInfos);

		if (CollectionUtils.isEmpty(groovyInfos)) {
			return;
		}

		destroyBeanDefinition(groovyInfos);

		initNew(groovyInfos);

		GroovyApiConfigCache.put2map(groovyInfos);

		refreshMapping(groovyInfos);
	}

	private void destroyBeanDefinition(List<GroovyApiConfig> groovyInfos) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext
				.getAutowireCapableBeanFactory();
		for (GroovyApiConfig groovyInfo : groovyInfos) {
			try {
				beanFactory.removeBeanDefinition(groovyInfo.getBeanName());
			} catch (Exception e) {
				System.out
						.println("【Groovy】 delete groovy bean definition exception. skip:" + groovyInfo.getBeanName());
			}
		}
	}

	/**
	 * 重建单一请求的注册与缓存
	 */
	public void refreshMapping(List<GroovyApiConfig> groovyScripts) {
		try {
			for (GroovyApiConfig apiInfo : groovyScripts) {
				if (apiInfo != null) {
					requestMappingService.unregisterMappingForApiConfig(apiInfo);
					GroovyApiConfigCache.remove(apiInfo);
				}

				if (apiInfo != null) {
					requestMappingService.registerMappingForApiConfig(apiInfo);
					GroovyApiConfigCache.put(apiInfo);
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}