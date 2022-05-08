package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;
import org.ssssssss.script.runtime.ExitValue;

public class ExitTests extends BaseTest {

	private Object doExecute(String filename) {
		Object object = execute(filename);
		assert object instanceof ExitValue;
		return object;
	}

	@Test
	public void exit_1() {
		Assert.assertArrayEquals(new Object[]{123, "hello", 0}, ((ExitValue) doExecute("grammar/exit/exit_1.ms")).getValues());
	}

	@Test
	public void exit_2() {
		Assert.assertArrayEquals(new Object[]{200, "success"}, ((ExitValue) doExecute("grammar/exit/exit_2.ms")).getValues());
	}
	@Test
	public void exit_3() {
		Assert.assertArrayEquals(new Object[]{400, "参数填写有误"}, ((ExitValue) doExecute("grammar/exit/exit_3.ms")).getValues());
	}
}
