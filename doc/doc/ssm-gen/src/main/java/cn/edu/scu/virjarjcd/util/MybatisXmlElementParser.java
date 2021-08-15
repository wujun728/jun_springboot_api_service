package cn.edu.scu.virjarjcd.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MybatisXmlElementParser {

	public static void main(String args[]) throws Exception{
		org.mybatis.generator.api.dom.xml.Document document = new MybatisXmlElementParser().parse(new FileInputStream("/Users/virjar/Desktop/codegentarget/testcodegen/src/main/resources/mapper/DataMapper.xml"));
		System.out.println(document.getFormattedContent());
	}
	
	public org.mybatis.generator.api.dom.xml.Document parse(String source) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		factory.setExpandEntityReferences(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setEntityResolver(new NullEntityResolver());

		Document document = builder.parse(new ByteArrayInputStream(source.getBytes()));

		DocumentType doctype = document.getDoctype();
		org.mybatis.generator.api.dom.xml.Document mybatisDocument = new org.mybatis.generator.api.dom.xml.Document(doctype.getPublicId(),doctype.getSystemId());
		Element rootElement = document.getDocumentElement();
		org.mybatis.generator.api.dom.xml.XmlElement mybatisElement = new org.mybatis.generator.api.dom.xml.XmlElement(rootElement.getNodeName());
		mybatisDocument.setRootElement(mybatisElement);
		parseElement(rootElement, mybatisElement);

		return mybatisDocument;
	}

	private void parseElement(Element rootElement,org.mybatis.generator.api.dom.xml.XmlElement mybatisElement){
		NamedNodeMap attributes = rootElement.getAttributes();
		int attributeCount = attributes.getLength();
		for (int i = 0; i < attributeCount; i++) {
			Node node = attributes.item(i);
			mybatisElement.addAttribute(new Attribute(node.getNodeName(), node.getNodeValue()));
		}
		
		NodeList childNodes = rootElement.getChildNodes();
		int childNodeCount = childNodes.getLength();
		for(int i = 0 ; i< childNodeCount; i++){
			Node item = childNodes.item(i);
			if(item instanceof Text){
				Text txtitem = (Text) item;
				String text = txtitem.getWholeText().trim();
				if(!StringUtils.isEmpty(text)){
					mybatisElement.addElement(new TextElement(text));
				}
			}else if(item instanceof Element){
				Element element = (Element) item;
				XmlElement mybatisElement2 = new XmlElement(element.getNodeName());
				mybatisElement.addElement(mybatisElement2);
				parseElement(element, mybatisElement2);
			}else{
				System.err.println("could not support:"+item.getClass());
			}
		}
	}
	
	public org.mybatis.generator.api.dom.xml.Document parse(InputStream is) throws Exception{
		return parse(IOUtils.toString(is));
	}
	private static class NullEntityResolver implements EntityResolver {
		/**
		 * returns an empty reader. This is done so that the parser doesn't
		 * attempt to read a DTD. We don't need that support for the merge and
		 * it can cause problems on systems that aren't Internet connected.
		 */
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {

			StringReader sr = new StringReader(""); //$NON-NLS-1$

			return new InputSource(sr);
		}
	}
}
