package com.jun.plugin.system;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * json,xml互相转换
 * @author libing
 *
 */
public class JsonXmlUtil {
	public static Map<String, Object> json2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if(jsonStr != null && !"".equals(jsonStr)){
            //最外层解析
            try {
                Map jsonObject = objectMapper.readValue(jsonStr, Map.class);
                for (Object k : jsonObject.keySet()) {
                    Object v = jsonObject.get(k);
                    //如果内层还是数组的话，继续解析
                    if (v instanceof ArrayList) {
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        Iterator<Map> it = ((ArrayList) v).iterator();
                        while (it.hasNext()) {
                            Map json2 = it.next();
                            list.add(json2Map(objectMapper.writeValueAsString(json2)));
                        }
                        map.put(k.toString(), list);
                    } else {
                        map.put(k.toString(), v);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map;
        }else{
            return null;
        }
    }
	
	
	public static String jsonToXml(Object json, String rootKey) {
	    Document document = DocumentHelper.createDocument();
	    //设置根节点和命名空间
	    QName qName = new QName(rootKey);
	    //设置命令空间
	    Element element = document.addElement(qName);
	    document.setRootElement(element);

	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        JSONObject jsonObject = new JsonParser().parse(objectMapper.writeValueAsString(json)).getAsJsonObject();
	        toXml(jsonObject, document.getRootElement(), null);
	        return document.asXML();
	    } catch (Exception e) {
	        log.error(e.getMessage());
	        throw new AppException(e.getMessage());
	    }
	}

	private static Element toXml(JsonElement jsonElement, Element parentElement, String name) {
	    if (jsonElement instanceof JsonArray) {
	        //是json数据，需继续解析
	        JsonArray sonJsonArray = (JsonArray)jsonElement;
	        for (int i = 0; i < sonJsonArray.size(); i++) {
	            JsonElement arrayElement = sonJsonArray.get(i);
	            toXml(arrayElement, parentElement, name);
	        }
	    }else if (jsonElement instanceof JsonObject) {
	        //说明是一个json对象字符串，需要继续解析
	        JsonObject sonJsonObject = (JsonObject) jsonElement;
	        Element currentElement = null;
	        if (name != null) {
	            currentElement = parentElement.addElement(name);
	        }
	        Set<Map.Entry<String, JsonElement>> set = sonJsonObject.entrySet();
	        for (Map.Entry<String, JsonElement> s : set) {
	            toXml(s.getValue(), currentElement != null ? currentElement : parentElement, s.getKey());
	        }
	    }else if (jsonElement.isJsonNull()){
	        parentElement.addElement(name);
	    }else {
	        //说明是一个键值对的key,可以作为节点插入了
	        addAttribute(parentElement, name, jsonElement.getAsString());
	    }
	    return parentElement;
	}

	private static void addAttribute(Element element, String name, String value) {
	    //增加子节点，并为子节点赋值
	    if (String.valueOf(name.charAt(0)).equals(StringPool.DASH)){

	        // 参数前为 “-”符号的为节点属性 去除“-”符号并添加属性
	        String key=name.substring(1);
	        if (key.equals("xmlns")){
	            Namespace namespace=new Namespace("",value);
	            QName qName=new QName(element.getName(),namespace);
	            element.setQName(qName);
	        }else {
	            element.addAttribute(key,value);
	        }
	    }else if (StringPool.HASH.equals(String.valueOf(name.charAt(0)))){
	        element.addText(value);
	    }else {
	        Element el = element.addElement(name);
	        el.addText(value);
	    }
	} 
}