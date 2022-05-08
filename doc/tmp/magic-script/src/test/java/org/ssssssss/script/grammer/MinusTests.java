package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;

public class MinusTests extends BaseTest {

	@Test
	public void minus_1(){
		Assert.assertEquals(0,execute("grammar/minus/minus_1.ms"));
	}
	@Test
	public void minus_2(){
		Assert.assertEquals(0.0,execute("grammar/minus/minus_2.ms"));
	}
	@Test
	public void minus_3(){
		Assert.assertEquals(-1.1,execute("grammar/minus/minus_3.ms"));
	}
	@Test
	public void minus_4(){
		Assert.assertEquals(0.0,execute("grammar/minus/minus_4.ms"));
	}
	@Test
	public void minus_5(){
		Assert.assertEquals(-1L,execute("grammar/minus/minus_5.ms"));
	}

	@Test
	public void minus_6(){
		Assert.assertEquals(-1L,execute("grammar/minus/minus_6.ms"));
	}
}
