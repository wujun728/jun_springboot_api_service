package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;

public class MulTests extends BaseTest {

	@Test
	public void mul_1(){
		Assert.assertEquals(1,execute("grammar/mul/mul_1.ms"));
	}
	@Test
	public void mul_2(){
		Assert.assertEquals(1.0,execute("grammar/mul/mul_2.ms"));
	}
	@Test
	public void mul_3(){
		Assert.assertEquals(0.2,execute("grammar/mul/mul_3.ms"));
	}
	@Test
	public void mul_4(){
		Assert.assertEquals(2.0,execute("grammar/mul/mul_4.ms"));
	}

	@Test
	public void mul_5(){
		Assert.assertEquals(2L,execute("grammar/mul/mul_5.ms"));
	}

	@Test
	public void mul_6(){
		Assert.assertEquals(6000L,execute("grammar/mul/mul_6.ms"));
	}
}
