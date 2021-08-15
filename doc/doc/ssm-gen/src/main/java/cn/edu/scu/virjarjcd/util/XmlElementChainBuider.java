package cn.edu.scu.virjarjcd.util;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;


public class XmlElementChainBuider {

	private String nodeName;
	private String textContent;
	private XmlElement xmlElement;
	private String textNode;
//	@Data
//	@AllArgsConstructor
//	private class Attribute{
//		private String name;
//		private String value;
//	}
	
	public XmlElementChainBuider(XmlElement xmlElement){
		this.xmlElement = xmlElement;
	}
	
	
	public XmlElementChainBuider(String nodeName){
		this.nodeName = nodeName;
	}
	
	private List<Attribute> attrs = new ArrayList<Attribute>();
	private List<XmlElementChainBuider> builderchains = new ArrayList<XmlElementChainBuider>();
	
	public XmlElementChainBuider setName(String name){
		this.nodeName = name;
		return this;
	}
	
	public XmlElementChainBuider addTextnode(String content){
		this.textNode = content;
		return this;
	}
	
	public XmlElementChainBuider addContent(String content){
		this.textContent = content;
		return this;
	}
	
	public XmlElementChainBuider addAttribute(String name,String value){
		this.attrs.add(new Attribute(name,value));
		return this;
	}
	
	public XmlElementChainBuider addNode(XmlElementChainBuider node){
		this.builderchains.add(node);
		return this;
	}
	
	public Element build(){
		if(this.textContent != null)
			return new TextElement(textContent);
		
		XmlElement rootElement = xmlElement;
		if(rootElement == null){ rootElement = new XmlElement(nodeName);}
		
		if(this.textNode != null){
			rootElement.addElement(new TextElement(textNode));
		}
		
		for(Attribute attribute:attrs){
			rootElement.addAttribute(attribute);
		}
		
		for(XmlElementChainBuider bulder:builderchains){
			rootElement.addElement(bulder.build());
		}
		return rootElement;
	}
}
