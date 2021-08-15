package cn.edu.scu.virjarjcd.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegexUtil {

	public static String extract(String source,String pattern, Integer group){
		if(StringUtils.isEmpty(source) || StringUtils.isEmpty(pattern)){
			return null;
		}
		Matcher matcher = Pattern.compile(pattern).matcher(source);
		if(matcher.find()){
			if(group == null) group =0;
			return matcher.group(group);
		}
		return null;
	}
	
	public static String extract(String source,Pattern pattern, Integer group){
		if(StringUtils.isEmpty(source) ){
			return null;
		}
		Matcher matcher = pattern.matcher(source);
		if(matcher.find()){
			if(group == null) group =0;
			return matcher.group(group);
		}
		return null;
	}
	
}
