package com.common.admin;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nh.micro.controller.MicroControllerPlugin;
import com.nh.micro.dao.mapper.MicroInjectDaoPlugin;
import com.nh.micro.dao.mapper.scan.BeanScannerConfigurer;
import com.nh.micro.db.MicroDbHolder;
import com.nh.micro.nhs.NhsInitUtil;
import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.context.MicroInjectPlugin;
import com.nh.micro.rule.engine.context.MicroInjectSpringPlugin;
import com.nh.micro.rule.engine.core.GFileBean;
import com.nh.micro.rule.engine.core.GroovyAopChain;
import com.nh.micro.rule.engine.core.GroovyInitUtil;
import com.nh.micro.rule.engine.core.GroovyLoadUtil;
import com.nh.micro.rule.engine.core.plugin.MicroAopPlugin;
import com.nh.micro.service.GroovyBeanScannerConfigurer;
import com.nh.micro.service.MicroInjectGroovyPlugin;

/**
 * 
 * @author ninghao
 *
 */

@Configuration
public class MicroAutoConfiguration implements EnvironmentAware {

	private Environment environment; 
	@Override 
	public void setEnvironment(final Environment environment) { this.environment = environment; } 


	@Bean
	@Lazy(false)
	public MicroControllerPlugin microControllerPlugin() {
		MicroControllerPlugin microControllerPlugin = new MicroControllerPlugin();

		return microControllerPlugin;
	}


	@Bean
	@Lazy(false)
	public MicroAopPlugin microAopPlugin() {
		MicroAopPlugin microAopPlugin = new MicroAopPlugin();

		return microAopPlugin;
	}

	@Bean
	@Lazy(false)
	public MicroInjectSpringPlugin microInjectSpringPlugin() {
		MicroInjectSpringPlugin microInjectSpringPlugin = new MicroInjectSpringPlugin();

		return microInjectSpringPlugin;
	}
	
	@Bean
	@Lazy(false)
	public MicroInjectPlugin microInjectPlugin() {
		MicroInjectPlugin microInjectPlugin = new MicroInjectPlugin();

		return microInjectPlugin;
	}

	@Bean
	@Lazy(false)
	public MicroInjectGroovyPlugin microInjectGroovyPlugin() {
		MicroInjectGroovyPlugin microInjectGroovyPlugin = new MicroInjectGroovyPlugin();

		return microInjectGroovyPlugin;
	}

	@Bean
	@Lazy(false)
	public MicroInjectDaoPlugin microInjectDaoPlugin() {
		MicroInjectDaoPlugin microInjectDaoPlugin = new MicroInjectDaoPlugin();
		return microInjectDaoPlugin;
	}

	@Bean
	@Lazy(false)
	public MicroContextHolder microContextHolder() {
		MicroContextHolder microContextHolder = new MicroContextHolder();
		return microContextHolder;
	}

	@Bean(initMethod = "init")
	@Lazy(false)
	public GroovyAopChain groovyAopChain() {
		GroovyAopChain groovyAopChain = new GroovyAopChain();
		return groovyAopChain;
	}

	@Bean
	@Lazy(false)
	public GroovyLoadUtil groovyLoadUtil() {
		GroovyLoadUtil groovyLoadUtil = new GroovyLoadUtil();
		groovyLoadUtil.getPluginList().add(microInjectSpringPlugin());
		groovyLoadUtil.getPluginList().add(microControllerPlugin());
		groovyLoadUtil.getPluginList().add(microAopPlugin());


		return groovyLoadUtil;
	}

	@Bean(initMethod = "initGroovyAndThread")
	@Lazy(false)
	public GroovyInitUtil groovyInitUtil() {
		GroovyInitUtil groovyInitUtil = new GroovyInitUtil();
		GFileBean gFileBean = new GFileBean();
		gFileBean.setDirFlag(Boolean.valueOf(environment.getProperty("nhmicro.config.dirFlag")));
		gFileBean.setJarFileFlag(Boolean.valueOf(environment.getProperty("nhmicro.config.jarFileFlag")));
		gFileBean.setRuleStamp(environment.getProperty("nhmicro.config.ruleStamp"));
		gFileBean.rulePath = environment.getProperty("nhmicro.config.rulePath");
		groovyInitUtil.fileList.add(gFileBean);
		return groovyInitUtil;

	}

	@Bean(initMethod = "initGroovyAndThread")
	@Lazy(false)
	public NhsInitUtil nhsInitUtil() {
		NhsInitUtil nhsInitUtil = new NhsInitUtil();
		GFileBean gFileBean = new GFileBean();
		gFileBean.setDirFlag(Boolean.valueOf(environment.getProperty("nhmicro.config.dirFlag")));
		gFileBean.setJarFileFlag(Boolean.valueOf(environment.getProperty("nhmicro.config.jarFileFlag")));
		gFileBean.setRuleStamp(environment.getProperty("nhmicro.config.ruleStamp"));
		gFileBean.rulePath = environment.getProperty("nhmicro.config.rulePath");
		nhsInitUtil.fileList.add(gFileBean);
		return nhsInitUtil;

	}	
	
	@Bean
	@Lazy(false)
	public MicroDbHolder microDbHolder(JdbcTemplate jdbcTemplate) {
		MicroDbHolder microDbHolder = new MicroDbHolder();
		microDbHolder.getDbHolder().put("default", jdbcTemplate);
		return microDbHolder;

	}

	@Bean
	@Lazy(false)
	@ConditionalOnProperty(prefix = "nhmicro.config",value="serviceScanPathFlag",  havingValue  = "true")	
	public GroovyBeanScannerConfigurer groovyBeanScannerConfigurer(){
		String scanPath=environment.getProperty("nhmicro.config.serviceScanPath");
		GroovyBeanScannerConfigurer groovyBeanScannerConfigurer=new GroovyBeanScannerConfigurer();
		groovyBeanScannerConfigurer.setScanPath(scanPath);
		return groovyBeanScannerConfigurer;
	}
	
	@Bean
	@Lazy(false)
	@ConditionalOnProperty(prefix = "nhmicro.config",value="daoScanPathFlag",  havingValue  = "true")
	public BeanScannerConfigurer daoBeanScannerConfigurer(){
		String scanPath=environment.getProperty("nhmicro.config.daoScanPath");
		BeanScannerConfigurer daoBeanScannerConfigurer=new BeanScannerConfigurer();
		daoBeanScannerConfigurer.setScanPath(scanPath);
		return daoBeanScannerConfigurer;
	}
}
