package cn.edu.scu.virjarjcd.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virjar on 16/3/31.
 */
public class TextNodeBuilder {

    private String rootName;
    private XmlElement rootElement;
    @Data
    @AllArgsConstructor
    private class ValuePair{
        private String nodeName;
        private String value;
    }

    private List<ValuePair> nodes = new ArrayList<>();
    public TextNodeBuilder addNode(String nodeName,String textValue){
        nodes.add(new ValuePair(nodeName,textValue));
        return this;
    }

    public TextNodeBuilder(XmlElement rootElement) {
        if(rootElement == null){
            throw new NullPointerException();
        }
        this.rootElement = rootElement;
    }

    public TextNodeBuilder(String rootName) {
        if(org.apache.commons.lang3.StringUtils.isBlank(rootName)){
            throw new NullPointerException("node Name cannot be blank");
        }
        this.rootName = rootName;
    }

    public XmlElement build(){
        if(rootElement == null){
            rootElement = new XmlElement(rootName);
        }
        for(ValuePair valuePair:nodes){
            XmlElement element = new XmlElement(valuePair.nodeName);
            element.addElement(new TextElement(valuePair.getValue()));
            rootElement.addElement(element);
        }
        return rootElement;
    }
}
