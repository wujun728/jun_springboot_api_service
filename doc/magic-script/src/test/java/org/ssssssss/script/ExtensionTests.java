package org.ssssssss.script;

import org.junit.Assert;
import org.junit.Test;

public class ExtensionTests extends BaseTest {

	@Test
	public void clazzNameTest(){
		Assert.assertEquals("java.util.Map$Entry,Entry,java.util.Map.Entry", execute("extension/clazz.ms"));
	}

}
