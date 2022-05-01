package org.ssssssss.script;

import org.junit.Test;

public class PerformanceTests extends BaseTest {

	@Test
	public void sum() {
		System.out.println(execute("performance/sum.ms"));
	}
}
