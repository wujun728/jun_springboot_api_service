package org.ssssssss.script.parsing.ast.binary;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Expression;

/**
 * !=、!==操作
 */
public class NotEqualOperation extends EqualOperation {

	public NotEqualOperation(Expression leftOperand, Span span, Expression rightOperand, boolean accurate) {
		super(leftOperand, span, rightOperand, accurate);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.visit(getLeftOperand())
				.visit(getRightOperand())
				.lineNumber(getSpan())
				.operator(accurate ? "not_accurate_equals" : "not_equals");
	}
}
