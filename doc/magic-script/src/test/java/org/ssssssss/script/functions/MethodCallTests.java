package org.ssssssss.script.functions;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ssssssss.script.BaseTest;
import org.ssssssss.script.MagicResourceLoader;
import org.ssssssss.script.reflection.JavaReflection;
import org.ssssssss.script.runtime.RuntimeContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class MethodCallTests extends BaseTest {

	private ClassType classType;

	public enum ClassType {
		NORMAL_CLASS,SERVICE_CLASS
	}

	public ClassType getClassType(){
		return classType;
	}

	@BeforeClass
	public static void register() {
		JavaReflection.registerMethodExtension(String.class, new MethodCallTests());
		JavaReflection.registerFunction(new MethodCallTests());
		MagicResourceLoader.addModule("test", new DynamicModuleImport(SimpleDateFormat.class, (context) -> new SimpleDateFormat("yyyyMMdd")));
		MagicResourceLoader.addModule("method", new MethodCallTests());
	}

	public static String call(String source, int val) {
		return "call_1_" + val;
	}

	public static String call(String source, double val) {
		return "call_2_" + val;
	}

	public static String call(String source) {
		return "call_3";
	}

	public static String call(String source, int ... vals) {
		return "call_4_" + Arrays.toString(vals);
	}

	public static String call(String source, User user) {
		return "call_5_" + user;
	}

	public static String call(String source, Supplier<String>... args) {
		return "call_6_" + Arrays.stream(args).map(Supplier::get).collect(Collectors.joining(","));
	}

	public static String call(String source, User ... users) {
		return "call_7_" + Arrays.toString(users);
	}

	public static String call(String source, Function<Object[], Object> function) {
		return "call_8_" + function.apply(new Object[0]);
	}

	public static String call(String source, RuntimeContext context, long a, long b) {
		return "call_9_" + (a + b);
	}

	public String call(RuntimeContext context, long a, long b) {
		return "call_10_" + (a + b);
	}

	public String call(RuntimeContext context, long a, long b, long ...c) {
		return "call_11_" + (a + b + LongStream.of(c).sum());
	}

	@Test
	public void method_call_1() {
		Assert.assertEquals("call_3", execute("functions/method_call_1.ms"));
	}

	@Test
	public void method_call_2() {
		Assert.assertEquals("call_1_1", execute("functions/method_call_2.ms"));
	}

	@Test
	public void method_call_3() {
		Assert.assertEquals("call_2_2.0", execute("functions/method_call_3.ms"));
	}

	@Test
	public void method_call_4() {
		Assert.assertEquals("call_4_[1, 2, 3]", execute("functions/method_call_4.ms"));
	}

	@Test
	public void method_call_5() {
		Assert.assertEquals("call_5_User{age=0, weight=0.0, money=0, roles=null, name='法外狂徒'}", execute("functions/method_call_5.ms"));
	}

	@Test
	public void method_call_6() {
		Assert.assertEquals("call_6_1,2,3", execute("functions/method_call_6.ms"));
	}

	@Test
	public void method_call_7() {
		Assert.assertEquals("call_7_[User{age=0, weight=0.0, money=0, roles=null, name='法外狂徒'}, User{age=0, weight=0.0, money=0, roles=null, name='张三'}]", execute("functions/method_call_7.ms"));
	}

	@Test
	public void method_call_8() {
		Assert.assertEquals("call_8_20210101", execute("functions/method_call_8.ms"));
	}

	@Test
	public void method_call_9() {
		Assert.assertEquals("call_9_3", execute("functions/method_call_9.ms"));
	}
	@Test
	public void method_call_10() {
		Assert.assertEquals("call_10_3", execute("functions/method_call_10.ms"));
	}

	@Test
	public void method_call_11() {
		Assert.assertEquals("call_11_10", execute("functions/method_call_11.ms"));
	}

	@org.ssssssss.script.annotation.Function
	public String function_call(String a, String b, String c){
		return a + b + c;
	}

	@Test
	public void function_call_test() {
		Assert.assertEquals("123", execute("functions/function_call.ms"));
	}

	@Test
	public void enum_test() {
		Assert.assertEquals(ClassType.NORMAL_CLASS, execute("functions/enum.ms"));
	}

}
