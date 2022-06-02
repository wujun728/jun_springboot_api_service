package com.google.tool;


import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

public class Tool_GroovyShell_3 {
	
	public static void main(String[] args) throws Exception {
		// GroovyScriptEngine的根路径，如果参数是字符串数组，说明有多个根路径
		GroovyScriptEngine engine = new GroovyScriptEngine("src/main/java/com/google/groovy/shell/");
		//GroovyScriptEngine engine = new GroovyScriptEngine(new String[] {"src/main/java/com/google/groovy/shell/"});
		
		Binding binding = new Binding();
		binding.setVariable("name", "google");
		
		Object result1 = engine.run("GroovyShell_3_1.groovy", binding);
		System.out.println(result1);
		Object result2 = engine.run("GroovyShell_3_2.groovy", binding);
		System.out.println(result2);
		Object result3 = engine.run("GroovyShell_3_3.groovy", binding);
		System.out.println(result3);
	}
	
	
}
