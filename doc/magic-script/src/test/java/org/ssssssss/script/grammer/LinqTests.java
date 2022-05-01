package org.ssssssss.script.grammer;

import org.junit.Assert;
import org.junit.Test;
import org.ssssssss.script.BaseTest;

public class LinqTests extends BaseTest {

	@Test
	public void linq_1() {
		execute("grammar/linq/linq_1.ms");
	}

	@Test
	public void linq_2() {
		Assert.assertEquals("[{a=1, count=6}]", execute("grammar/linq/linq_2.ms"));
	}

	@Test
	public void linq_3() {
		Assert.assertEquals("[{a=1, b=2, c=3}]", execute("grammar/linq/linq_3.ms"));
	}

	@Test
	public void linq_4() {
		Assert.assertEquals("[{sex=0, name=小明}]", execute("grammar/linq/linq_4.ms"));
	}

	@Test
	public void linq_5() {
		Assert.assertEquals("{sex=0, name=小明}", execute("grammar/linq/linq_5.ms"));
	}

	@Test
	public void linq_6() {
		Assert.assertEquals("[{sex=1, name=小花}]", execute("grammar/linq/linq_6.ms"));
	}

	@Test
	public void linq_7() {
		Assert.assertEquals("[{id=100, group_name=组1, names=A,B}, {id=101, group_name=组2, names=C}]", execute("grammar/linq/linq_7.ms"));
	}

	@Test
	public void linq_8() {
		Assert.assertEquals("[{bname=B_1, cname=C_1}, {bname=B_1, cname=C_1}, {bname=B_2, cname=C_2}, {bname=B_2, cname=C_2}, {bname=B_3, cname=C_3}, {bname=B_3, cname=C_3}, {bname=B_10, cname=C_10}, {bname=B_20, cname=C_20}, {bname=B_30, cname=C_30}, {bname=null, cname=C_40}]", execute("grammar/linq/linq_8.ms"));
	}

	@Test
	public void linq_9() {
		execute("grammar/linq/linq_9.ms");
	}

	@Test
	public void linq_10() {
		Assert.assertEquals("[{aname=A1, bname=B1, cname=C1}, {aname=A2, bname=B22, cname=C22}]", execute("grammar/linq/linq_10.ms"));
	}
}
