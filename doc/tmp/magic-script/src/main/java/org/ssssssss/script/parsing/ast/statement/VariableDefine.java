package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.VarIndex;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.Node;

public class VariableDefine extends Node {

	private final Expression right;

	private final VarIndex varIndex;

	public VariableDefine(Span span, VarIndex varIndex, Expression right) {
		super(span);
		this.varIndex = varIndex;
		this.right = right;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		if (right != null) {
			right.visitMethod(compiler);
		}
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.pre_store(varIndex)
				.visit(right)    // 读取变量值
				.scopeStore();    // 保存变量
	}

	public VarIndex getVarIndex() {
		return varIndex;
	}
}
