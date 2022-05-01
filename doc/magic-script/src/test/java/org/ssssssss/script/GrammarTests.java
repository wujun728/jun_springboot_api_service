package org.ssssssss.script;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.runtime.ExitValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GrammarTests extends BaseTest {

	@Test
	public void binary() {
		execute("grammar/binary.ms");
	}

	@Test
	public void increment() {
		System.out.println(execute("grammar/increment.ms"));
	}

	@Test
	public void varTest() {
		execute("grammar/var.ms");
	}

	@Test
	public void tryTest() {
		Assert.assertEquals(0, execute("grammar/try.ms"));
	}

	@Test
	public void try2Test() {
		Assert.assertEquals("trycatchclose2close1finally83311", execute("grammar/try2.ms"));
	}
	public static class AutoClose implements AutoCloseable {
		private final StringBuilder sb;
		private final String v;

		public AutoClose(StringBuilder sb, String v) {
			this.sb = sb;
			this.v = v;
		}

		@Override
		public void close() throws Exception {
			sb.append("close");
			sb.append(v);
		}
	}

	@Test
	public void newTest() {
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), execute("grammar/new.ms"));
	}

	@Test
	public void new1Test() {
		Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), execute("grammar/new_1.ms"));
	}

	@Test
	public void forTest() {
		Assert.assertEquals(2700, execute("grammar/for.ms"));
	}

	@Test
	public void escapeTest() {
		execute("grammar/escape.ms");
	}

	@Test
	public void lambdaTest() {
		Assert.assertEquals(8, execute("grammar/lambda.ms"));
	}

	@Test
	public void loopListTest() {
		Assert.assertEquals(15, execute("grammar/loopList.ms"));
	}

	@Test
	public void loop1() {
		Assert.assertEquals(2700, execute("grammar/loop_1.ms"));
	}

	@Test
	public void async() {
		Assert.assertEquals(5050.0, execute("grammar/async.ms"));
	}

	@Test
	public void loopMapTest() {
		Assert.assertEquals("key1key2key3key4k5-15", execute("grammar/loopMap.ms"));
	}

	@Test
	public void recursionTest() {
		Assert.assertEquals(55, execute("grammar/recursion.ms"));
	}

	@Test
	public void var_scope_1_test() {
		Assert.assertEquals(3, execute("grammar/var_scope_1.ms"));
	}

	@Test
	public void var_scope_2_test() {
		Assert.assertEquals(13, execute("grammar/var_scope_2.ms"));
	}

	@Test
	public void var_scope_3_test() {
		Assert.assertEquals(1, execute("grammar/var_scope_3.ms"));
	}

	@Test
	public void spread() {
		Assert.assertEquals(true, execute("grammar/spread.ms"));
	}

	@Test
	public void list_method_call() {
		Assert.assertEquals(5, execute("grammar/list_method_call.ms"));
	}

	@Test
	public void lambda_call_method_call() {
		Assert.assertEquals("12666", execute("grammar/lambda_call_method_call.ms"));
	}

	@Test
	public void method_call() {
		Assert.assertEquals("23.00", execute("grammar/method_call.ms"));
	}

	@Test
	public void optional_call() {
		Assert.assertEquals("truetruetrue", execute("grammar/optional.ms"));
	}

	@Test
	public void ternary() {
		Assert.assertEquals("成年", execute("grammar/ternary.ms"));
	}

	@Test
	public void regexp() {
		execute("grammar/regexp.ms");
	}

	@Test
	public void while_1() {
		Assert.assertEquals(4950, execute("grammar/while_1.ms"));
	}

	@Test
	public void importTest() {
		Assert.assertEquals(true, execute("grammar/import.ms"));
	}

	@Test
	public void castTest() {
		Assert.assertEquals(true, execute("grammar/cast.ms"));
	}

	@Test
	public void equalTest() {
		Assert.assertEquals(true, execute("grammar/equal.ms"));
	}
	@Test
	public void functionalTest() {
		execute("grammar/functional.ms");
	}

	@Test
	public void assertTest() {
		Object value = execute("grammar/assert.ms");
		Assert.assertTrue(value instanceof ExitValue);
		Assert.assertArrayEquals(new Object[]{2, 3, 4}, ((ExitValue) value).getValues());
	}

	@Test
	public void arrayTest() {
		Assert.assertEquals(true, execute("grammar/array.ms"));
	}


	@Test
	public void templateStringTest() {
		Assert.assertEquals("111222333", execute("grammar/template_string.ms"));
	}

	@Test
	public void assertInstanceOf() {
		Object value = execute("grammar/instanceOf.ms");
		Assert.assertEquals(Boolean.TRUE, value);
	}

	@Test
	public void assertDestructuring() {
		Assert.assertEquals(10, execute("grammar/destructuring.ms"));
	}
}
