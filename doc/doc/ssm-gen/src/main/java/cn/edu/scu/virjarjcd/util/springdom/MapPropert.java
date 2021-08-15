package cn.edu.scu.virjarjcd.util.springdom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class MapPropert extends AbstractBean {

	private HashMap<String,AbstractBean> map = new HashMap<String,AbstractBean>();
	
	public void addEntry(String key,String value){
		this.map.put(key, new TextPropert(value));
	}
	@Override
	public boolean isTextNode() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public XmlElement buildBeanElement() {
		// TODO Auto-generated method stub
		XmlElement xmlElement = new XmlElement("map");
		Iterator<Entry<String, AbstractBean>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, AbstractBean> next = iterator.next();
			XmlElement entry = new XmlElement("entry");
			entry.addAttribute(new Attribute("key", next.getKey()));
			entry.addAttribute(new Attribute("value", next.getValue().getContent()));
			xmlElement.addElement(entry);
		}
		return xmlElement;
	}

}
