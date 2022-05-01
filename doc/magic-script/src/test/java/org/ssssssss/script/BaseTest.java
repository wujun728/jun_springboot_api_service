package org.ssssssss.script;

import org.ssssssss.script.exception.MagicExitException;
import org.ssssssss.script.runtime.MagicScriptRuntime;

import java.io.InputStream;
import java.util.Arrays;

public class BaseTest {

	public static String readScript(String filename) {
		try (InputStream is = BaseTest.class.getResourceAsStream("/" + filename)) {
			byte[] buf = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int len = -1;
			while ((len = is.read(buf, 0, buf.length)) != -1) {
				sb.append(new String(buf, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object execute(String filename) {
		String str = readScript(filename);
		long t = System.currentTimeMillis();
		MagicScript script = MagicScript.create(str, null);
		script.compile();
		System.out.println("编译耗时：" + (System.currentTimeMillis() - t) + "ms");
		t = System.currentTimeMillis();
		MagicScriptContext context = new MagicScriptContext();
		context.setScriptName(filename);
		Object value = script.execute(context);
		System.out.println("执行耗时：" + (System.currentTimeMillis() - t) + "ms");
		System.out.println("执行结果：" + value);
		return value;
	}
}
