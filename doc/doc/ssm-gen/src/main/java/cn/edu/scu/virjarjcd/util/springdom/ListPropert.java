package cn.edu.scu.virjarjcd.util.springdom;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.xml.XmlElement;

public class ListPropert extends AbstractBean {

	private List<AbstractBean> beans = new ArrayList<AbstractBean>();
	
	public void addBean(AbstractBean bean){
		this.beans.add(bean);
	}
	@Override
	public boolean isTextNode() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public XmlElement buildBeanElement() {
		// TODO Auto-generated method stub
		XmlElement xmlElement = new XmlElement("list");
		for(AbstractBean bean :beans){
			xmlElement.addElement(bean.buildBeanElement());
		}
		return xmlElement;
	}

	
}
