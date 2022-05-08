package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.exception.MagicScriptRuntimeException;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.Node;

public class Throw extends Node {

	private final Expression expression;

	public Throw(Span span, Expression expression) {
		super(span);
		this.expression = expression;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		expression.visitMethod(compiler);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.visit(expression)
				.invoke(INVOKESTATIC, MagicScriptRuntimeException.class, "create", MagicScriptRuntimeException.class, Object.class)
				.insn(ATHROW);
	}
}
