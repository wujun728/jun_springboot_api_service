package org.ssssssss.script.compile;

import org.junit.Test;
import org.ssssssss.script.BaseTest;
import org.ssssssss.script.runtime.handle.OperatorHandle;

import java.util.ArrayList;
import java.util.List;

public class CompileTests extends BaseTest {

	@Test
	public void defineVar() {
		System.out.println(execute("compile/var.ms"));
	}

	@Test
	public void operator() {
		System.out.println(execute("compile/operator.ms"));
	}

	@Test
	public void compile_combine_assign() {
		System.out.println(execute("compile/compile_combine_assign.ms"));
	}

}
