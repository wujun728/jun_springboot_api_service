package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.VarIndex;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.VariableSetter;

public class VariableAccess extends Expression implements VariableSetter {

	private final VarIndex varIndex;

	public VariableAccess(Span name, VarIndex varIndex) {
		super(name);
		this.varIndex = varIndex;
	}

	public VarIndex getVarIndex() {
		return varIndex;
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.load(varIndex);
	}
}