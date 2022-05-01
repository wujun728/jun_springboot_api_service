package org.ssssssss.script.parsing.ast.binary;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.BinaryOperation;
import org.ssssssss.script.parsing.ast.Expression;

/**
 * ==、===操作
 */
public class EqualOperation extends BinaryOperation {

	protected final boolean accurate;

	public EqualOperation(Expression leftOperand, Span span, Expression rightOperand, boolean accurate) {
		super(leftOperand, span, rightOperand);
		this.accurate = accurate;
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.visit(getLeftOperand())
				.visit(getRightOperand())
				.lineNumber(getSpan())
				.operator(accurate ? "accurate_equals" : "equals");
	}
}
