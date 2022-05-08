package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;

public class DivTests extends BaseTest {

	@Test
	public void div_1(){
		Assert.assertEquals(1,execute("grammar/div/div_1.ms"));
	}
	@Test
	public void div_2(){
		Assert.assertEquals(1.0,execute("grammar/div/div_2.ms"));
	}
	@Test
	public void div_3(){
		Assert.assertEquals(0.0,execute("grammar/div/div_3.ms"));
	}
	@Test
	public void div_4(){
		Assert.assertEquals(0.5,execute("grammar/div/div_4.ms"));
	}

	@Test
	public void div_5(){
		Assert.assertEquals(0L,execute("grammar/div/div_5.ms"));
	}

	@Test
	public void div_6(){
		Assert.assertEquals(100L,execute("grammar/div/div_6.ms"));
	}
}
