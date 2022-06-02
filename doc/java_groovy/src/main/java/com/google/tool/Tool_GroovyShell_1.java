package com.google.tool;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

public class Tool_GroovyShell_1 {
	
	private static Logger logger = Logger.getLogger(Tool_GroovyShell_1.class);

	public static void main(String[] args) throws Exception {
		testGroovy3();
	}
	
	// 调用evaluate方法直接执行一段Groovy
	public static void testGroovy1() throws CompilationFailedException, IOException {
		GroovyShell groovyShell = new GroovyShell();
		groovyShell.evaluate("println 'My First Groovy shell.'");
	}
	
	// 调用GroovyShell_1_1
	public static void testGroovy2() throws CompilationFailedException, IOException {
		GroovyShell groovyShell = new GroovyShell();
		Object result = groovyShell.evaluate(new File("src/main/java/com/google/groovy/GroovyShell_1_1.groovy"));
		logger.info(result.toString());
	}
	
	// 调用GroovyShell_1_2
	public static void testGroovy3() throws CompilationFailedException, IOException {
		// 调用带参数的groovy shell时，使用bind绑定数据
		Binding binding = new Binding();
		binding.setProperty("name", "google");
		
		GroovyShell groovyShell = new GroovyShell(binding);
		Object result = groovyShell.evaluate(new File("src/main/java/com/google/groovy/GroovyShell_1_2.groovy"));
		logger.info(result.toString());
	}
	
}
