package cn.edu.scu.virjarjcd.util;

import cn.edu.scu.virjarjcd.exception.CodegenException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Properties;


public class FileUtils extends org.apache.commons.io.FileUtils {


	
	public static boolean isDirExist(String filepath){
		File file = new File(filepath);
		return (file.exists() && file.isDirectory());
	}
	
	public static boolean isFileExist(String filepath){
		File file = new File(filepath);
		return (file.exists() && file.isFile());
	}
	
	public static boolean createIfNotExist(String dirpath){
		File file = new File(dirpath);
		if(file.exists()){
			if(!file.isDirectory())
				return false;
			return true;
		}else{
			return file.mkdirs();
		}
	}
	
	public static String package2path(String packageName){
		String[] strings = packageName.split("\\.");
		StringBuilder sb = new StringBuilder();
		for(String str:strings){
			sb.append(str);
			sb.append(File.separator);
		}
		return sb.toString();
	}
	
	public static String getJavaFilePath(String project,String packageName,String modelName){
		if(!project.endsWith("/")){
			project = project+"/";
		}
		return project + package2path(packageName)+modelName+".java";
	}
	public static Properties readXmlJavaSettingsFile(File file, Properties properties, String profile) {
		int defaultSize = properties.size();
		if (!file.exists()) {
			throw new CodegenException("file not fond for:"+file.getAbsolutePath());
		}
		if (profile == null) {
			throw new IllegalStateException("no profile selected, go to settings and select proper settings file");
		}
		boolean profileFound = false;
		try { // load file profiles
			org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			doc.getDocumentElement().normalize();

			NodeList profiles = doc.getElementsByTagName("profile");
			if (profiles.getLength() == 0) {
				throw new IllegalStateException(
						"loading of profile settings failed, file does not contain any profiles");
			}
			for (int temp = 0; temp < profiles.getLength(); temp++) {
				Node profileNode = profiles.item(temp);
				if (profileNode.getNodeType() == Node.ELEMENT_NODE) {
					Element profileElement = (Element) profileNode;
					String name = profileElement.getAttribute("name");
					if (profile.equals(name)) {
						profileFound = true;
						NodeList childNodes = profileElement.getElementsByTagName("setting");
						if (childNodes.getLength() == 0) {
							throw new IllegalStateException(
									"loading of profile settings failed, profile has no settings elements");
						}
						for (int i = 0; i < childNodes.getLength(); i++) {
							Node item = childNodes.item(i);
							if (item.getNodeType() == Node.ELEMENT_NODE) {
								Element attributeItem = (Element) item;
								String id = attributeItem.getAttribute("id");
								String value = attributeItem.getAttribute("value");
								properties.setProperty(id.trim(), value.trim());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!profileFound) {
			throw new IllegalStateException("profile not found in the file " + file.getAbsolutePath());
		}
		if (properties.size() == defaultSize) {
			throw new IllegalStateException("no properties loaded, something is broken, file:" + file.getAbsolutePath());
		}
		return properties;
	}

}
