package cn.edu.scu.virjarjcd.util.springdom;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class NormalBean extends AbstractBean {

	@Override
	public boolean isTextNode() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public NormalBean(String clazz){
		this.clazz = clazz;
	}
	
	@Override
	public XmlElement buildBeanElement(){
		XmlElement xmlElement = new XmlElement("bean");
		xmlElement.addAttribute(new Attribute("class", clazz));
		for(Propert propert:properties){
			XmlElement propertElement = new XmlElement("property");
			xmlElement.addElement(propertElement);
			propertElement.addAttribute(new Attribute("name", propert.getName()));
			AbstractBean subBean = propert.getValue();
			if(subBean.isTextNode()){
				propertElement.addAttribute(new Attribute("value", subBean.getContent()));
			}else{
				propertElement.addElement(subBean.buildBeanElement());
			}
		}
		return xmlElement;
	}
}
