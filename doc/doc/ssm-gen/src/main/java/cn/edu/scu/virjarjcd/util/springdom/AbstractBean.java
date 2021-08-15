package cn.edu.scu.virjarjcd.util.springdom;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.mybatis.generator.api.dom.xml.XmlElement;

public abstract class AbstractBean {

	@Setter
	protected String clazz;
	//for text value(not bean list map...)
	@Setter
	@Getter
	private String content;
	protected List<Propert> properties = new ArrayList<Propert>();
	public abstract boolean isTextNode();
	public void addPropert(Propert propert){
		this.properties.add(propert);
	}
	
	public abstract XmlElement buildBeanElement();
}
