package org.ssssssss.script.parsing.ast;

import org.ssssssss.script.compile.MagicScriptCompiler;

public interface VariableSetter {

	default void compile_visit_variable(MagicScriptCompiler compiler) {
		throw new UnsupportedOperationException("暂不支持编译" + this.getClass().getSimpleName());
	}
}