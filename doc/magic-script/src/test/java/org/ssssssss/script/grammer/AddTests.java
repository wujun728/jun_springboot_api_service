package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;

public class AddTests extends BaseTest {

	@Test
	public void add_1(){
		Assert.assertEquals(2,execute("grammar/add/add_1.ms"));
	}
	@Test
	public void add_2(){
		Assert.assertEquals(2.0,execute("grammar/add/add_2.ms"));
	}
	@Test
	public void add_3(){
		Assert.assertEquals(3.1,execute("grammar/add/add_3.ms"));
	}
	@Test
	public void add_4(){
		Assert.assertEquals(3.1,execute("grammar/add/add_4.ms"));
	}
	@Test
	public void add_5(){
		Assert.assertEquals(3L,execute("grammar/add/add_5.ms"));
	}
	@Test
	public void add_6(){
		Assert.assertEquals(3L,execute("grammar/add/add_6.ms"));
	}
}
