package cn.edu.scu.virjarjcd.util.springdom;

import org.mybatis.generator.api.dom.xml.XmlElement;

import cn.edu.scu.virjarjcd.exception.CodegenException;

public class TextPropert extends AbstractBean {

	public TextPropert(String content){
		this.setContent(content);
	}
	
	@Override
	public boolean isTextNode() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public XmlElement buildBeanElement() {
		// TODO Auto-generated method stub
		throw new CodegenException("could not build Element for Text Node");
	}

}
