package com.nh.micro.orm;



import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author ninghao
 *
 */
public class MicroBeanMapUtil {

	public static Map getFieldMap(Class cls) {
		Map map = new HashMap();
		Field[] fields = cls.getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			MicroMappingAnno anno = field.getAnnotation(MicroMappingAnno.class);
			if (anno == null) {
				continue;
			}
			String keyName = anno.name();
			String fieldName = field.getName();
			map.put(fieldName, keyName);
		}
		return map;
	}

	public static Map getKeyMap(Class cls) {
		Map map = new HashMap();
		Field[] fields = cls.getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			MicroMappingAnno anno = field.getAnnotation(MicroMappingAnno.class);
			if (anno == null) {
				continue;
			}
			String keyName = anno.name();
			String fieldName = field.getName();
			map.put(keyName, fieldName);
		}
		return map;
	}

	/*
	 * @param beanObj
	 * 
	 * @param fieldName
	 * 
	 * @param value
	 */
	public static void setBeanProperty(Object beanObj, String fieldName,
			Object value) throws Exception {
		Class cls = beanObj.getClass();
		Field field = cls.getDeclaredField(fieldName);
		Class fieldCls = field.getType();
		field.setAccessible(true);
		Object fieldObj = str2Obj(fieldCls, value);
		field.set(beanObj, fieldObj);
	}
	
	public static Object getBeanProperty(Object beanObj, String fieldName) throws Exception {
		Class cls = beanObj.getClass();
		Field field = cls.getDeclaredField(fieldName);
		Class fieldCls = field.getType();
		field.setAccessible(true);
		Object retVal=field.get(beanObj);
		Object retObj=obj2Str(fieldCls,retVal);
		return retObj;
	}
	
	

	public static Object str2Obj(Class cls, Object val) throws Exception {
		if(val==null){
			return null;
		}
		Object retObj = null;
		if (cls.isAssignableFrom(String.class)) {
			retObj = new String((String) val);
		}
		if (cls.isAssignableFrom(Integer.class) || cls.isAssignableFrom(int.class)) {
			retObj = Integer.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Long.class) || cls.isAssignableFrom(long.class)) {
			retObj = Long.valueOf((String) val);
		}
		if (cls.isAssignableFrom(BigDecimal.class)) {
			retObj = new BigDecimal((String) val);
		}
		if (cls.isAssignableFrom(Float.class) || cls.isAssignableFrom(float.class)) {
			retObj = Float.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Double.class) || cls.isAssignableFrom(double.class)) {
			retObj = Double.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tempVal=(String) val;
			if(tempVal.length()==10){
				tempVal=tempVal+" 00:00:00";
			}
			retObj = (Date)sdf.parseObject(tempVal);
		}
		
		if (cls.isAssignableFrom(Boolean.class) || cls.isAssignableFrom(boolean.class)) {
			retObj = Boolean.valueOf((String) val);
		}
		if (cls.isAssignableFrom(List.class)) {
			retObj = val;
		}
		if (cls.isAssignableFrom(Short.class) || cls.isAssignableFrom(short.class)) {
			retObj = Short.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Byte.class) || cls.isAssignableFrom(byte.class)) {
			retObj = Byte.valueOf((String) val);
		}		
		
		if (cls.isAssignableFrom(Character.class) || cls.isAssignableFrom(char.class)) {
			retObj = Character.valueOf(((String)val).charAt(0));
		}		
		if (cls.isAssignableFrom(Map.class)) {
			retObj = val;
		}
		
		return retObj;
	}

	
	public static Object obj2Str(Class cls, Object val) throws Exception {
		if(val==null){
			return null;
		}
		Object retObj = null;
		if (cls.isAssignableFrom(String.class)) {
			retObj = new String((String) val);
		}
		if (cls.isAssignableFrom(Integer.class) || cls.isAssignableFrom(int.class) ) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Long.class) || cls.isAssignableFrom(long.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(BigDecimal.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Float.class) || cls.isAssignableFrom(float.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Double.class) || cls.isAssignableFrom(double.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			retObj=sdf.format(val);
		}
		
		if (cls.isAssignableFrom(Boolean.class) || cls.isAssignableFrom(boolean.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(List.class)) {
			retObj = val;
		}
		if (cls.isAssignableFrom(Short.class) || cls.isAssignableFrom(short.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Byte.class) || cls.isAssignableFrom(byte.class)) {
			retObj = val.toString();
		}	

		if (cls.isAssignableFrom(Character.class) || cls.isAssignableFrom(char.class)) {
			retObj = val.toString();
		}		
		if (cls.isAssignableFrom(Map.class)) {
			retObj = val;
		}
		
		return retObj;
	}	
	
	public static Object mapToBean(Map paramMap, Class cls) throws Exception {
		if(paramMap==null){
			return null;
		}
		if(checkPrim(cls)){
			if(paramMap.isEmpty()){
				return null;
			}
			Set set=paramMap.entrySet();
			Object[] objArray=set.toArray();
			Entry entry=(Entry) objArray[0];
			Object val=entry.getValue();
			return str2Obj(cls,val);
		}
		if(cls.isAssignableFrom(Map.class)){
			return paramMap;
		}
		Object retObj = cls.newInstance();
		mapToBean(paramMap,retObj);
		return retObj;
	}
	
	public static boolean checkPrim(Class cls){
		if(cls.isPrimitive()){
			return true;
		}
		if(cls.isAssignableFrom(String.class)){
			return true;
		}
		if(cls.isAssignableFrom(Date.class)){
			return true;
		}	
		if(cls.isAssignableFrom(BigDecimal.class)){
			return true;
		}		
		return false;
	}
	
	public static void mapToBean(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			if (mappingInfo.get(key) == null) {
				continue;
			}
			Object val = paramMap.get(key);
			String fieldName = (String) mappingInfo.get(key);
			setBeanProperty(retObj, fieldName, val);
		}

	}	
	
	public static void mapToBean4NotNull(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			if (mappingInfo.get(key) == null) {
				continue;
			}
			Object val = paramMap.get(key);
			if(val==null){
				continue;
			}
			String fieldName = (String) mappingInfo.get(key);
			setBeanProperty(retObj, fieldName, val);
		}

	}	
	
	public static void mapToBean4FullField(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = mappingInfo.keySet();
		for (String mapKey : keySet) {
			Object val = paramMap.get(mapKey);
			String fieldName = (String) mappingInfo.get(mapKey);
			setBeanProperty(retObj, fieldName, val);
		}
	}	
	
	public static void beanToMap(Object beanObj, Map paramMap) throws Exception {
		Class cls=beanObj.getClass();
		Map fieldMap = getFieldMap(cls);

		Set<String> keySet = fieldMap.keySet();
		for (String fieldKey : keySet) {

			String mapKey = (String) fieldMap.get(fieldKey);
			Object val=getBeanProperty(beanObj, fieldKey);
			paramMap.put(mapKey, val);
		}
	}	

	public static void beanToMap4NotNull(Object beanObj, Map paramMap) throws Exception {
		Class cls=beanObj.getClass();
		Map fieldMap = getFieldMap(cls);

		Set<String> keySet = fieldMap.keySet();
		for (String fieldKey : keySet) {

			String mapKey = (String) fieldMap.get(fieldKey);
			Object val=getBeanProperty(beanObj, fieldKey);
			if(val==null){
				continue;
			}
			paramMap.put(mapKey, val);
		}
	}	
	
	public static Map beanToMap(Object beanObj) throws Exception {
		Map retMap=new HashMap();
		beanToMap(beanObj,retMap);
		return retMap;

	}
	

	
}
