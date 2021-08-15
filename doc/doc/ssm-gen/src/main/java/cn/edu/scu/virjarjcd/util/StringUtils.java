package cn.edu.scu.virjarjcd.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class StringUtils
{

	private static final Random random = new Random();

	public static String getDefaultTestClassPkg(String path)
	{
		StringBuffer buffer = new StringBuffer();
		if ((null != path) && (-1 != path.indexOf("."))) {
			for (String p : path.split("\\.")) {
				buffer.append(File.separator).append(p).append(File.separator);
			}
		}
		return buffer.toString();
	}

	public static boolean startsWith(String param, String prefix)
	{
		return param.startsWith(prefix);
	}

	public static String firstLetterCaps(String param)
	{
		if ((null == param) || ("".equals(param))) {
			return "";
		}
		String firstLetter = String.valueOf(param.charAt(0));
		if (firstLetter.matches("[a-zA-Z]")) {
			firstLetter = firstLetter.toUpperCase();
		}
		return new StringBuffer().append(firstLetter).append(param.substring(1)).toString();
	}

	public static boolean equalsIgnoreCase(String str1, String str2)
	{
		if (str1 != null) {
			return str1.equalsIgnoreCase(str2);
		}
		return false;
	}

	public static String firstLetterLows(String param)
	{
		if ((null == param) || ("".equals(param))) {
			return "";
		}
		String firstLetter = String.valueOf(param.charAt(0));
		if (firstLetter.matches("[a-zA-Z]")) {
			firstLetter = firstLetter.toLowerCase();
		}
		return new StringBuffer().append(firstLetter).append(param.substring(1)).toString();
	}

	public static Set<String> getKeys(Map<String, String> paramMap)
	{
		return paramMap.keySet();
	}

	public static String getRandomId()
	{
		double d1 = Math.random();
		double d2 = Math.random();
		double d3 = Math.random();
		return new StringBuilder().append(String.valueOf((long)(d1 * d2 * d3 * 10000.0D))).append("L").toString();
	}

	public static String getSampleValueByType(String fieldType, int size)
	{
		if (fieldType.equalsIgnoreCase("string")) {
			String str = UUID.randomUUID().toString();
			return new StringBuilder().append("\"").append(str.substring(0, size <= str.length() ? size : str.length())).append("\"").toString();
		}if (fieldType.equalsIgnoreCase("long"))
			return new StringBuilder().append(String.valueOf(randLong())).append("L").toString();
		if ((fieldType.equalsIgnoreCase("int")) || (fieldType.equalsIgnoreCase("Integer")))
			return String.valueOf(randInt());
		if (fieldType.equalsIgnoreCase("boolean"))
			return String.valueOf(random.nextBoolean());
		if (fieldType.equalsIgnoreCase("double"))
			return String.valueOf(randDouble());
		if (fieldType.equalsIgnoreCase("date"))
			return String.valueOf("new Date()");
		if (fieldType.equalsIgnoreCase("BigDecimal"))
			return new StringBuilder().append("new BigDecimal(").append(String.valueOf(randLong())).append("L)").toString();
		if (fieldType.equalsIgnoreCase("float"))
			return String.valueOf(randFloat());
		if (fieldType.equalsIgnoreCase("short")) {
			return String.valueOf(randShort());
		}
		throw new RuntimeException(new StringBuilder().append("Failed to generate smaple value for field type: ").append(fieldType).toString());
	}

	public static int randInt()
	{
		return random.nextInt(90000001) + 10000000;
	}

	public static long randLong() {
		return random.nextInt(900000001) + 100000000;
	}

	public static float randFloat() {
		return random.nextFloat() + 1.0E+008F;
	}

	public static short randShort() {
		return (short)random.nextInt();
	}

	public static double randDouble() {
		return random.nextDouble();
	}

	public static String genClassWithFolder(String outputpath, String clzzname) {
		return new StringBuilder().append(outputpath).append("\\").append(firstLetterCaps(clzzname)).append("Test.java").toString();
	}

	public static String getBasePackage(String servicePackage)
	{
		int lastDotPos = servicePackage.lastIndexOf(".");
		return servicePackage.substring(0, lastDotPos);
	}

	public static String cutBy(String source,char from,char to,int startnum){
		int startoff =-1;
		int endoff = -1;
		int i =0;
		
		//find first offset
		while(startnum >0){
			for(;i<source.length();i++){
				if(source.charAt(i) == from){
					startnum --;
					break;
				}
			}
			if(i>= source.length()){
				return null;
			}
		}
		startoff = i;
		
	
		for(;i<source.length();i++){
			if(source.charAt(i) == to){
				endoff = i;
				break;
			}
		}
		if(i >= source.length()){
			return null;
		}
		return source.substring(startoff, endoff);
	}
	public static String betterMatching(String order1, String order2, String anImport) {
		if (order1.equals(order2)) {
			throw new IllegalArgumentException("orders are same");
		}
		for (int i = 0; i < anImport.length() - 1; i++) {
			if (order1.length() - 1 == i && order2.length() - 1 != i) {
				return order2;
			}
			if (order2.length() - 1 == i && order1.length() - 1 != i) {
				return order1;
			}
			char orderChar1 = order1.length() != 0 ? order1.charAt(i) : ' ';
			char orderChar2 = order2.length() != 0 ? order2.charAt(i) : ' ';
			char importChar = anImport.charAt(i);

			if (importChar == orderChar1 && importChar != orderChar2) {
				return order1;
			} else if (importChar != orderChar1 && importChar == orderChar2) {
				return order2;
			}

		}
		return null;
	}

	public static List<String> trimToList(String importOrder1) {
		ArrayList<String> strings = new ArrayList<String>();
		String[] split = importOrder1.split(";");
		for (String s : split) {
			String trim = s.trim();
			if (!trim.isEmpty()) {
				strings.add(trim);
			}
		}
		return strings;
	}

	public static List<String> trimImports(String imports) {
		String[] split = imports.split("\n");
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (s.startsWith("import ")) {
				s = s.substring(7, s.indexOf(";"));
				strings.add(s.trim());
			}
		}
		return strings;
	}

	public static List<String> trimImports(List<String> imports) {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < imports.size(); i++) {
			String s = imports.get(i);
			if (s.startsWith("import ")) {
				s = s.substring(7, s.indexOf(";"));
				strings.add(s.trim());
			} else {
				strings.add(s.trim());
			}
		}
		return strings;
	}



	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}