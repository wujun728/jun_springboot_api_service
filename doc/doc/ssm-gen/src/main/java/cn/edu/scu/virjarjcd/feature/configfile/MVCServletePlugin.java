package cn.edu.scu.virjarjcd.feature.configfile;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import cn.edu.scu.virjarjcd.util.springdom.ListPropert;
import cn.edu.scu.virjarjcd.util.springdom.MapPropert;
import cn.edu.scu.virjarjcd.util.springdom.NormalBean;
import cn.edu.scu.virjarjcd.util.springdom.Propert;
import cn.edu.scu.virjarjcd.util.springdom.TextPropert;

public class MVCServletePlugin extends MergePluginAdpter {
	private String webAppPath ;
	private String basePackae;
	private XmlFormatter springXmlFormatter;

	private String restUtilPackage;
	private String exceptinUtilPackage;


	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		// TODO Auto-generated method stub
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		Document document = new Document(null, null);
		XmlElement rootElement = new XmlElement("beans");
		GeneratedXmlFile gxf = new GeneratedXmlFile(document,"mvc-servlet.xml", "",webAppPath+"/WEB-INF",true, springXmlFormatter);
		answer.add(gxf);
		document.setRootElement(rootElement);

		addBeansAttr(rootElement);
		addBeans(rootElement);

//		rootElement.addElement(
//				new XmlElementChainBuider("context:component-scan")
//				.addAttribute("base-package", basePackae)
//				.build());

		addControllerScan(rootElement);
		rootElement.addElement(new XmlElement("context:annotation-config"));

		addAnnotationDriven(rootElement);
		return answer;
	}

	private void addControllerScan(XmlElement rootElement){
		new XmlElementChainBuider(rootElement)
		.addNode(
				new XmlElementChainBuider("context:component-scan")
						.addAttribute("base-package", basePackae + ".**.controller")
//取消自定义注解对controller的作用，暂时不需要自定义controller功能
//				.addAttribute("use-default-filters", "false")
//				.addNode(
//						new XmlElementChainBuider("context:include-filter")
//						.addAttribute("type", "annotation")
//						.addAttribute("expression", "org.springframework.stereotype.Controller")
//						)
//				.addNode(
//						new XmlElementChainBuider("context:include-filter")
//						.addAttribute("type", "annotation")
//						.addAttribute("expression", restUtilPackage+".RestApiController")
//						)
//				.addNode(
//						new XmlElementChainBuider("context:include-filter")
//						.addAttribute("type", "annotation")
//						.addAttribute("expression", "org.springframework.stereotype.Controller")
//						)
		)
		.build();

//		new XmlElementChainBuider(rootElement)
//		.addNode(
//				new XmlElementChainBuider("bean")
//				.addAttribute("id", "restApiControllerHandlerMapping")
//				.addAttribute("class", restUtilPackage+".RestApiControllerHandlerMapping")
//				)
//		.build();

		new XmlElementChainBuider(rootElement)
		.addNode(
				new XmlElementChainBuider("bean")
						//.addAttribute("id", "restApiResponseEntityExceptionHandler")
						.addAttribute("class", exceptinUtilPackage + ".RestApiResponseEntityExceptionHandler")
//				.addNode(
//						new XmlElementChainBuider("property")
//						.addAttribute("name", "messageSource")
//						.addAttribute("ref", "messageSource")
//						)
//				.addNode(
//						new XmlElementChainBuider("property")
//						.addAttribute("name", "logAsError")
//						.addAttribute("value", "false")
//						)
		)
		.build();

		new XmlElementChainBuider(rootElement)
				.addNode(
						new XmlElementChainBuider("bean")
								.addAttribute("class", exceptinUtilPackage + ".BadRequestExceptionHandler")
//						.addNode(
//								new XmlElementChainBuider("property")
//								.addAttribute("name","order")
//								.addAttribute("value","0")
//						)
				)
				.build();
	}

	private void addAnnotationDriven(XmlElement rootElement){
		rootElement.addElement(
				new XmlElementChainBuider("mvc:annotation-driven")
						.addAttribute("content-negotiation-manager", "contentNegotiationManager")
						.addNode(
								new XmlElementChainBuider("mvc:argument-resolvers")
										.addNode(
												new XmlElementChainBuider("bean")
														.addAttribute("class", "org.springframework.data.web.PageableHandlerMethodArgumentResolver")
										)
						)
						.addNode(
								new XmlElementChainBuider("mvc:message-converters")
										.addAttribute("register-defaults", "true")
										.addNode(
												new XmlElementChainBuider("bean")
														.addAttribute("class", "org.springframework.http.converter.StringHttpMessageConverter")
														.addNode(
																new XmlElementChainBuider("property")
																		.addAttribute("name", "supportedMediaTypes")
																		.addAttribute("value", "text/plain;charset=UTF-8")
														)
										)
						)
				.build()
				);
	}

	public static void main(String args[]){
		new MVCServletePlugin().addBeans(new XmlElement("test"));
	}

	private void addBeans(XmlElement rootElement){
		new XmlElementChainBuider(rootElement).addNode(
				new XmlElementChainBuider("bean")
				.addAttribute("id", "contentNegotiationManager")
				.addAttribute("class", "org.springframework.web.accept.ContentNegotiationManagerFactoryBean")
				.addNode(
						new XmlElementChainBuider("property")
								.addAttribute("name", "mediaTypes")
								.addNode(
										new XmlElementChainBuider("value")
												.addTextnode("html=text/html\njson=application/json")
								)
				).addNode(
						new XmlElementChainBuider("property")
								.addAttribute("name", "defaultContentType")
								.addAttribute("value", "text/html")
				)
		).build();

		new XmlElementChainBuider(rootElement)
				.addNode(
						new XmlElementChainBuider("bean")
						.addAttribute("class","org.springframework.web.servlet.view.ContentNegotiatingViewResolver")
						.addNode(
								new XmlElementChainBuider("property")
								.addAttribute("name","contentNegotiationManager")
								.addAttribute("ref","contentNegotiationManager")
						)
						.addNode(
								new XmlElementChainBuider("property")
								.addAttribute("name","viewResolvers")
								.addNode(
										new XmlElementChainBuider("list")
										.addNode(
												new XmlElementChainBuider("bean")
												.addAttribute("class","org.springframework.web.servlet.view.BeanNameViewResolver")
										)
										.addNode(
												new XmlElementChainBuider("bean")
												.addAttribute("class","org.springframework.web.servlet.view.InternalResourceViewResolver")
												.addNode(
														new XmlElementChainBuider("property")
														.addAttribute("name","prefix")
														.addAttribute("value","/WEB-INF/views/")
												)
												.addNode(
														new XmlElementChainBuider("property")
														.addAttribute("name","suffix")
														.addAttribute("value",".jsp")
												)
												.addNode(
														new XmlElementChainBuider("property")
														.addAttribute("name","viewClass")
														.addAttribute("value","org.springframework.web.servlet.view.JstlView")
												)
										)
								)
						)
						.addNode(
								new XmlElementChainBuider("property")
								.addAttribute("name","defaultViews")
								.addNode(
										new XmlElementChainBuider("list")
										.addNode(
												new XmlElementChainBuider("bean")
												.addAttribute("class","org.springframework.web.servlet.view.json.MappingJackson2JsonView")
										)
								)
						)
				)
				.build();

//		NormalBean normalBean = new NormalBean("org.springframework.web.servlet.view.ContentNegotiatingViewResolver");
//		//normalBean.setClazz("org.springframework.web.servlet.view.ContentNegotiatingViewResolver");
//		normalBean.addPropert(new Propert("ignoreAcceptHeader", new TextPropert("false")));
//		normalBean.addPropert(new Propert("defaultContentType", new TextPropert("text/html")));
//
//		MapPropert mediaTypesMap = new MapPropert();
//		mediaTypesMap.addEntry("html", "text/html");
//		mediaTypesMap.addEntry("json", "application/json");
//		normalBean.addPropert(new Propert("mediaTypes", mediaTypesMap));
//
//
//		ListPropert viewResolversList = new ListPropert();
//		viewResolversList.addBean(new NormalBean("org.springframework.web.servlet.view.BeanNameViewResolver"));
//
//		NormalBean jspRsolver =new NormalBean("org.springframework.web.servlet.view.InternalResourceViewResolver");
//		jspRsolver.addPropert(new Propert("prefix", new TextPropert("/WEB-INF/views/")));
//		jspRsolver.addPropert(new Propert("suffix", new TextPropert(".jsp")));
//		jspRsolver.addPropert(new Propert("viewClass", new TextPropert("org.springframework.web.servlet.view.JstlView")));
//
//		viewResolversList.addBean(jspRsolver);
//		normalBean.addPropert(new Propert("viewResolvers", viewResolversList));
//
//
//		ListPropert defaultViewsList = new ListPropert();
//		defaultViewsList.addBean(new NormalBean("org.springframework.web.servlet.view.json.MappingJackson2JsonView"));
//		normalBean.addPropert(new Propert("defaultViews", defaultViewsList));
//
//		rootElement.addElement(normalBean.buildBeanElement());
	}

	public static void addBeansAttr(XmlElement rootElement){
		rootElement.addAttribute(new Attribute("xmlns",
				"http://www.springframework.org/schema/beans"));
		rootElement.addAttribute(new Attribute("xmlns:mvc",
				"http://www.springframework.org/schema/mvc"));
		rootElement.addAttribute(new Attribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance"));
		rootElement.addAttribute(new Attribute("xmlns:context",
				"http://www.springframework.org/schema/context"));
		StringBuilder schemaLocation = new StringBuilder();
		schemaLocation.append("http://www.springframework.org/schema/beans ");
		schemaLocation.append("http://www.springframework.org/schema/beans/spring-beans.xsd ");
		schemaLocation.append("http://www.springframework.org/schema/context ");
		schemaLocation.append("http://www.springframework.org/schema/context/spring-context.xsd ");
		schemaLocation.append("http://www.springframework.org/schema/mvc ");
		schemaLocation.append("http://www.springframework.org/schema/mvc/spring-mvc.xsd");

		rootElement.addAttribute(new Attribute("xsi:schemaLocation", schemaLocation.toString()));
	}

	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		webAppPath = ConfigHolder.instance.getProperty("sys.webappRootPath")+"/src/main/webapp";
		ConfigHolder.instance.setProperty("sys.webAppPath", webAppPath);

		restUtilPackage = ConfigHolder.instance.getProperty("responseEnvelpe.package");
		exceptinUtilPackage = ConfigHolder.instance.getProperty("core.exception.package");

		FileUtils.createIfNotExist(webAppPath+"/WEB-INF");

		basePackae = ConfigHolder.instance.getProperty("sys.basePackage", "com.virjar");

		if(ConfigHolder.instance.getProperty("formater.spring", "mybatis").trim().equalsIgnoreCase("jcg")){
			springXmlFormatter = new VirjarXmlFormater();
			springXmlFormatter.setContext(getContext());
		}else{
			springXmlFormatter = getContext().getXmlFormatter();
		}

		return true;
	}

}
