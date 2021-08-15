package cn.edu.scu.virjarjcd.mybatis.formater.xml;

import java.util.List;

import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

import cn.edu.scu.virjarjcd.exception.CodegenException;

/**
 * mybatis gen自带的formater对于没有非TextElement的代码格式化，格式化策略和一般文件不一致
 * 本类重写此方法，可以应用在pom.xml logback.xml web.xml web-mvc.xml appplicationContext.xml
 */
public class VirjarXmlFormater implements XmlFormatter {

	protected Context context;
	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
	}

	@Override
	public String getFormattedContent(Document document) {
		// TODO Auto-generated method stub
		return formatDocument(document);
	}

	private String formatDocument(Document document){
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); //$NON-NLS-1$

		if (document.getPublicId() != null && document.getSystemId() != null) {
			OutputUtilities.newLine(sb);
			sb.append("<!DOCTYPE "); //$NON-NLS-1$
			sb.append(document.getRootElement().getName());
			sb.append(" PUBLIC \""); //$NON-NLS-1$
			sb.append(document.getPublicId());
			sb.append("\" \""); //$NON-NLS-1$
			sb.append(document.getSystemId());
			sb.append("\" >"); //$NON-NLS-1$
		}

		OutputUtilities.newLine(sb);
		sb.append(formatXmlElement(document.getRootElement(),0));
		return sb.toString();
	}

	private String formatXmlElement(XmlElement xmlElement,int indentLevel){
		StringBuilder sb = new StringBuilder();

		OutputUtilities.xmlIndent(sb, indentLevel);
		sb.append('<');
		sb.append(xmlElement.getName());

		for (Attribute att : xmlElement.getAttributes()) {
			sb.append(' ');
			sb.append(att.getFormattedContent());
		}
		List<Element> elements = xmlElement.getElements();
		if (elements.size() > 0) {
			sb.append(">"); //$NON-NLS-1$
			boolean neednewline = true;
			for (Element element : elements) {
				if(element instanceof TextElement){
					TextElement textElement = (TextElement) element;
					sb.append(textElement.getContent());
					neednewline = false;
				}else if(element instanceof XmlElement){
					OutputUtilities.newLine(sb);
					sb.append(formatXmlElement((XmlElement)element, indentLevel + 1));
					neednewline = true;
				}else{
					throw new CodegenException("jcg xml formater did not support "+element.getClass()+" element");
				}
			}
			if(neednewline){
				OutputUtilities.newLine(sb);
				OutputUtilities.xmlIndent(sb, indentLevel);
			}
			sb.append("</"); //$NON-NLS-1$
			sb.append(xmlElement.getName());
			sb.append('>');

		} else {
			sb.append(" />"); //$NON-NLS-1$
		}

		return sb.toString();
	}
}
