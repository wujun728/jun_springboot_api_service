package cn.edu.scu.virjarjcd.feature.model;


import cn.edu.scu.virjarjcd.codegertator.VirjarFileGenerator;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class ModeGerator extends  VirjarFileGenerator{

	@Override
	public String targetProject() {
		// TODO Auto-generated method stub
		return ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath");
	}

	@Override
	public String targetPackage() {
		// TODO Auto-generated method stub
		return ConfigHolder.instance.getProperty("sys.basePackage")+".model";
	}

	@Override
	public String suffrex() {
		// TODO Auto-generated method stub
		return "Model";
	}

}
