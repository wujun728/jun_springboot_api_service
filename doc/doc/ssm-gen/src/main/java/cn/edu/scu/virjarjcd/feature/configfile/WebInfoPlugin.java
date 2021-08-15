package cn.edu.scu.virjarjcd.feature.configfile;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;

public class WebInfoPlugin extends MergePluginAdpter {

	private String webAppPath ;
	private String projectName;
	private XmlFormatter webXmlFormatter;


	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		// TODO Auto-generated method stub
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		 Document document = new Document(null, null);
		XmlElement rootElement = new XmlElement("web-app"); 
		GeneratedXmlFile gxf = new GeneratedXmlFile(document,"web.xml", "",webAppPath+"/WEB-INF",true, webXmlFormatter);
		answer.add(gxf);
		document.setRootElement(rootElement);
		
		rootElement.addAttribute(new Attribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance"));
		rootElement.addAttribute(new Attribute("xmlns",
				"http://java.sun.com/xml/ns/j2ee"));
		
		rootElement.addAttribute(new Attribute("xsi:schemaLocation",
				"http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"));
		rootElement.addAttribute(new Attribute("version",
				"2.4"));
		
		
        context.getCommentGenerator().addRootComment(rootElement);
        
        XmlElement display_nameElement = new XmlElement("display-name");
        display_nameElement.addElement(new TextElement(projectName+"-webapp"));
        rootElement.addElement(display_nameElement);
        
        addContextParam(rootElement);
        addListener(rootElement);
        addFilter(rootElement);
        addFilterMapper(rootElement);
        addServlet(rootElement);
        addServletMapping(rootElement);
        
        return answer;
	}
	
	private void addServletMapping(XmlElement rootElement){
		rootElement.addElement(new ServletMappingNode("mvc", "/").buildXmlElement());
		if(!"spring".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.datasource"))) {
			rootElement.addElement(new ServletMappingNode("DruidStatView", "/druid/*").buildXmlElement());
		}
		rootElement.addElement(new ServletMappingNode("default", "*.css").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.gif").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.jpg").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.js").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.html").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.htm").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.png").buildXmlElement());
		rootElement.addElement(new ServletMappingNode("default", "*.chm").buildXmlElement());
	}
	
	@Data
	@AllArgsConstructor
	private static  class ServletMappingNode{
		private String name;
		private String pattern;
		public XmlElement buildXmlElement(){
			XmlElement answer = new XmlElement("servlet-mapping");
			
			XmlElement servlet_name = new XmlElement("servlet-name");
			servlet_name.addElement(new TextElement(name));
			answer.addElement(servlet_name);
			
			XmlElement url_pattern = new XmlElement("url-pattern");
			url_pattern.addElement(new TextElement(pattern));
			answer.addElement(url_pattern);
			return answer;
		}
	}
	
	private void addServlet(XmlElement rootElement){
		XmlElement mvcServlet = new XmlElement("servlet");
		XmlElement servletName = new XmlElement("servlet-name");
		servletName.addElement(new TextElement("mvc"));
		mvcServlet.addElement(servletName);
		
		XmlElement servletClazz = new XmlElement("servlet-class");
		servletClazz.addElement(new TextElement("org.springframework.web.servlet.DispatcherServlet"));
		mvcServlet.addElement(servletClazz);
		
		XmlElement load_on_startup = new XmlElement("load-on-startup");
		load_on_startup.addElement(new TextElement("1"));
		mvcServlet.addElement(load_on_startup);
		
		rootElement.addElement(mvcServlet);

		if(!"spring".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.datasource"))){
			new XmlElementChainBuider(rootElement)
					.addNode(
							new XmlElementChainBuider("servlet")
							.addNode(
									new XmlElementChainBuider("servlet-name").addTextnode("DruidStatView")
							)
							.addNode(
									new XmlElementChainBuider("servlet-class").addTextnode("com.alibaba.druid.support.http.StatViewServlet")
							)
					)
					.build();
		}
	}

	private void addFilterMapper(XmlElement rootElement){
		XmlElement hiddenHttpMethodFilter = new XmlElement("filter-mapping");
		XmlElement filterNameElement = new XmlElement("filter-name");
		filterNameElement.addElement(new TextElement("HiddenHttpMethodFilter"));
		hiddenHttpMethodFilter.addElement(filterNameElement);
		
		XmlElement servletElement = new XmlElement("servlet-name");
		servletElement.addElement(new TextElement("mvc"));
		hiddenHttpMethodFilter.addElement(servletElement);
		
		rootElement.addElement(hiddenHttpMethodFilter);
		
		XmlElement encodingFilter = new XmlElement("filter-mapping");
		filterNameElement = new XmlElement("filter-name");
		filterNameElement.addElement(new TextElement("encodingFilter"));
		encodingFilter.addElement(filterNameElement);
		
		XmlElement urlPattern = new XmlElement("url-pattern");
		urlPattern.addElement(new TextElement("/*"));
		encodingFilter.addElement(urlPattern);
		
		rootElement.addElement(encodingFilter);
	}
	
	private static class FilterNode{
		@Getter
		@Setter
		private String name;
		@Getter
		@Setter
		private String clazz;
		@Getter
		@Setter
		private String urlPattern;
		private List<InitParam> initParams = new ArrayList<InitParam>();
		
		public void addParam(InitParam initParam){
			this.initParams.add(initParam);
		}
		@Data
		@AllArgsConstructor
		private static class InitParam{
			private String Name;
			private String value;
		}
		public XmlElement buildXmlElement(){
			XmlElement answer = new XmlElement("filter");
			XmlElement filter_name = new XmlElement("filter-name");
			filter_name.addElement(new TextElement(name));
			answer.addElement(filter_name);
			
			XmlElement clazzElement = new XmlElement("filter-class");
			clazzElement.addElement(new TextElement(clazz));
			answer.addElement(clazzElement);
			
			for(InitParam initParam:initParams){
				XmlElement initParamElement = new XmlElement("init-param");
				XmlElement nameElement = new XmlElement("param-name");
				nameElement.addElement(new TextElement(initParam.getName()));
				
				XmlElement valueElement = new XmlElement("param-value");
				valueElement.addElement(new TextElement(initParam.getValue()));
				
				initParamElement.addElement(nameElement);
				initParamElement.addElement(valueElement);
			}
			
			return answer;
		}
	}
	
	private void addFilter(XmlElement xmlElement){
		FilterNode encodingFilter = new FilterNode();
		encodingFilter.setName("encodingFilter");
		encodingFilter.setClazz("org.springframework.web.filter.CharacterEncodingFilter");
		encodingFilter.addParam(new FilterNode.InitParam("encoding","UTF-8"));
		encodingFilter.addParam(new FilterNode.InitParam("forceEncoding", "true"));
		xmlElement.addElement(encodingFilter.buildXmlElement());
		
		FilterNode hiddenHttpMethodFilter = new FilterNode();
		hiddenHttpMethodFilter.setName("HiddenHttpMethodFilter");
		hiddenHttpMethodFilter.setClazz("org.springframework.web.filter.HiddenHttpMethodFilter");
		xmlElement.addElement(hiddenHttpMethodFilter.buildXmlElement());
	}
	
	private void addListener(XmlElement xmlElement){
		XmlElement listener = new XmlElement("listener");
		XmlElement listener_class = new XmlElement("listener-class");
//		listener_class.addElement(new TextElement("org.springframework.web.util.Log4jConfigListener"));
//		xmlElement.addElement(listener);
//		listener.addElement(listener_class);
		
		listener_class = new XmlElement("listener-class");
		listener_class.addElement(new TextElement("org.springframework.web.context.ContextLoaderListener"));
		listener = new XmlElement("listener");
		listener.addElement(listener_class);
		
		xmlElement.addElement(listener);
	}
	
	private void addContextParam(XmlElement xmlElement){
		XmlElement contextParamElment = new XmlElement("context-param");
		XmlElement paramNameElement = new XmlElement("param-name");
		paramNameElement.addElement(new TextElement("contextConfigLocation"));
		contextParamElment.addElement(paramNameElement);
		
		XmlElement paramValueElement = new XmlElement("param-value");
		paramValueElement.addElement(new TextElement("classpath:/applicationContext.xml"));
		contextParamElment.addElement(paramValueElement);
		xmlElement.addElement(contextParamElment);
		
		contextParamElment = new XmlElement("context-param");
		paramNameElement = new XmlElement("param-name");
		paramNameElement.addElement(new TextElement("spring.profiles.default"));
		contextParamElment.addElement(paramNameElement);
		
		paramValueElement = new XmlElement("param-value");
		paramValueElement.addElement(new TextElement("production"));
		contextParamElment.addElement(paramValueElement);
		xmlElement.addElement(contextParamElment);
		
	}
	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		webAppPath = ConfigHolder.instance.getProperty("sys.webappRootPath")+"/src/main/webapp";
		ConfigHolder.instance.setProperty("sys.webAppPath", webAppPath);
		
		projectName = ConfigHolder.instance.getProperty("sys.projectName");
		FileUtils.createIfNotExist(webAppPath+"/WEB-INF");
		
		if(ConfigHolder.instance.getProperty("formater.webxml", "mybatis").trim().equalsIgnoreCase("jcg")){
			webXmlFormatter = new VirjarXmlFormater();
			webXmlFormatter.setContext(getContext());
		}else{
			webXmlFormatter = getContext().getXmlFormatter();
		}
		return true;
	}
}
