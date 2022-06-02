package com.common.admin;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;

import com.common.util.ImageServlet;
import com.nh.micro.controller.MicroControllerServlet;
import com.nh.micro.controller.MicroPageServlet;



@Configuration
//@ImportResource("classpath:root-context.xml")
public class SpringConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}


	/**
	 * 多个servlet就注册多个bean
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean1() {
		return new ServletRegistrationBean(new ImageServlet(), "/servlet/imgCheckCode");// ServletName默认值为首字母小写，即myServlet
	}

	// 统一页码处理配置
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/WEB-INF/nh-micro-jsp/404.jsp");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
						"/WEB-INF/nh-micro-jsp/500.jsp");
				container.addErrorPages(error404Page, error500Page);
			}
		};
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean2() {
		MicroPageServlet microPageServlet=new MicroPageServlet();
		microPageServlet.setPrepath("micropage");
		return new ServletRegistrationBean(microPageServlet, "/micropage/*");// ServletName默认值为首字母小写，即myServlet
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean3() {
		MicroControllerServlet microControllerServlet=new MicroControllerServlet();
		microControllerServlet.setPrepath("micromvc");
		return new ServletRegistrationBean(microControllerServlet, "/micromvc/*");// ServletName默认值为首字母小写，即myServlet
	}
}
