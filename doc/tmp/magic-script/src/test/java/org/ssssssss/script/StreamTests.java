package org.ssssssss.script;

import org.junit.Assert;
import org.junit.Test;

public class StreamTests extends BaseTest {

	@Test
	public void eachTest(){
		System.out.println(execute("stream/each.ms"));
	}

	@Test
	public void distinctTest(){
		Assert.assertEquals("[1, 2, 3, 5, 6][1, 2, 3, 5]",execute("stream/distinct.ms"));
	}
}
