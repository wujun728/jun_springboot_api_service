package cn.edu.scu.virjarjcd.codegertator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.config.PluginConfiguration;

import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class SysPluginCollector {

	private List<PluginConfiguration> plugins = new ArrayList<PluginConfiguration>();
	public List<PluginConfiguration> getplugin(){
		return plugins;
	}
	
	public SysPluginCollector calcute(){
		this.plugins.clear();
		if(ConfigHolder.instance.getProperty("lombok", "true").equalsIgnoreCase("true")){
			PluginConfiguration pluginConfiguration = new PluginConfiguration();
			pluginConfiguration.setConfigurationType("cn.edu.scu.virjarjcd.feature.lombok.LombokGenPlugin");
			this.plugins.add(pluginConfiguration);
		}
		return this;
	}
}
