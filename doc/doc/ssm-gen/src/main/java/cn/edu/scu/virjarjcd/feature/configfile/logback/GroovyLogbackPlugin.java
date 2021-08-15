package cn.edu.scu.virjarjcd.feature.configfile.logback;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class GroovyLogbackPlugin extends MergePluginAdpter{

	private String resourcepath;
	private String projectName;
	private String basePackage;

	
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		resourcepath = ConfigHolder.instance.getProperty("sys.resourcepath");
		projectName = ConfigHolder.instance.getProperty("sys.projectName");
		basePackage = ConfigHolder.instance.getProperty("sys.basePackage");
		return true;
	}
}
