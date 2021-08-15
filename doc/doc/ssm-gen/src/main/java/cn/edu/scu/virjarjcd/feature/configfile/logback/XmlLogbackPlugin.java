package cn.edu.scu.virjarjcd.feature.configfile.logback;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;

public class XmlLogbackPlugin extends MergePluginAdpter {

	private String resourcepath;
	private String projectName;
	private String basePackage;
	private String pattern;
	private String appender;
	private String baseFolder;
	private XmlFormatter xmlFormatter;
	
	
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		// TODO Auto-generated method stub
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
		Document document = new Document(null, null);
		XmlElement rootElement = new XmlElement("configuration"); 
		document.setRootElement(rootElement);
		GeneratedXmlFile gxf = new GeneratedXmlFile(document,"logback.xml", "",resourcepath,true, xmlFormatter);
		answer.add(gxf);
		
		addAppender(rootElement);
		addLogger(rootElement);
		return answer;
	}
	
	private void addLogger(XmlElement xmlElement){
		new XmlElementChainBuider(xmlElement)
		.addNode(
				new XmlElementChainBuider("logger")
				.addAttribute("name", this.basePackage)
				.addAttribute("level", "info")
				.addNode(
						new XmlElementChainBuider("appender-ref")
						.addAttribute("ref", "console")
						)
				.addNode(
						new XmlElementChainBuider("appender-ref")
						.addAttribute("ref", "file")
						)
				)
		.addNode(
				new XmlElementChainBuider("root")
				.addAttribute("level", "info")
				.addNode(
						new XmlElementChainBuider("appender-ref")
						.addAttribute("ref", "console")
						)
				)
		.build();
	}
	
	private void addAppender(XmlElement xmlElement){
		new XmlElementChainBuider(xmlElement)
		.addNode(
				new XmlElementChainBuider("appender")
				.addAttribute("name", "console")
				.addAttribute("class", "ch.qos.logback.core.ConsoleAppender")
				.addNode(
						new XmlElementChainBuider("encoder")
						.addAttribute("charset", "UTF-8")
						.addNode(
								new XmlElementChainBuider("pattern")
								.addTextnode(pattern)
								)
						)
				)
		.build();
		
		new XmlElementChainBuider(xmlElement)
		.addNode(
				new XmlElementChainBuider("appender")
				.addAttribute("name", "file")
				.addAttribute("class", appender)
				.addNode(
						new XmlElementChainBuider("encoder")
						.addAttribute("charset", "UTF-8")
						.addNode(
								new XmlElementChainBuider("pattern")
								.addTextnode(pattern)
								)
						)
				.addNode(
						new XmlElementChainBuider("rollingPolicy")
						.addAttribute("class", "ch.qos.logback.core.rolling.TimeBasedRollingPolicy")
						.addNode(
								new XmlElementChainBuider("fileNamePattern")
								.addTextnode(baseFolder+"info.%d{yyyy-MM-dd}.log")
								)
						.addNode(
								new XmlElementChainBuider("maxHistory")
								.addTextnode("30")
								)
						)
				)
		.build();
	}

	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		resourcepath = ConfigHolder.instance.getProperty("sys.webapp.resoucepath");
		projectName = ConfigHolder.instance.getProperty("sys.projectName");
		basePackage = ConfigHolder.instance.getProperty("sys.basePackage");
		
		pattern = ConfigHolder.instance.getProperty("logger.pattern");
		if(StringUtils.isEmpty(pattern)){
			pattern="%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class{5}:%line>>%msg%n";
		}
		
		appender = ConfigHolder.instance.getProperty("logger.appender");
		
		if(StringUtils.isEmpty(appender)){
			appender = "ch.qos.logback.core.rolling.RollingFileAppender";
		}
		
		baseFolder = ConfigHolder.instance.getProperty("logger.basefolder");
		if(StringUtils.isEmpty(baseFolder)){
			baseFolder = "${catalina.base}/logs/"+this.projectName+"/";
		}
		
		if(ConfigHolder.instance.getProperty("formater.spring", "mybatis").trim().equalsIgnoreCase("jcg")){
			xmlFormatter = new VirjarXmlFormater();
			xmlFormatter.setContext(getContext());
		}else{
			xmlFormatter = getContext().getXmlFormatter();
		}
		
		return true;
	}
}
