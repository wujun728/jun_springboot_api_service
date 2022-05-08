package org.ssssssss.script.parsing.ast.binary;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.BinaryOperation;
import org.ssssssss.script.parsing.ast.Expression;

/**
 * &
 */
public class BitAndOperation extends BinaryOperation {

	public BitAndOperation(Expression leftOperand, Span span, Expression rightOperand) {
		super(leftOperand, span, rightOperand);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.visit(getLeftOperand())
				.visit(getRightOperand())
				.lineNumber(getSpan())
				.bit("and");
	}
}
