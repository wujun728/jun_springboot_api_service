package cn.edu.scu.virjarjcd.feature.vo;


import cn.edu.scu.virjarjcd.codegertator.VirjarFileGenerator;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class VoGerator extends VirjarFileGenerator {

	@Override
	public String targetProject() {
		// TODO Auto-generated method stub
		return ConfigHolder.instance.getProperty("sys.webapp.javasourcepath");
	}

	@Override
	public String targetPackage() {
		// TODO Auto-generated method stub
		return ConfigHolder.instance.getProperty("sys.basePackage")+".vo";
	}

	@Override
	public String suffrex() {
		// TODO Auto-generated method stub
		return "VO";
	}

}
