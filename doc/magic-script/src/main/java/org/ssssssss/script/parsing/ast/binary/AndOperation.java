package org.ssssssss.script.parsing.ast.binary;

import org.ssssssss.script.asm.Label;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.BinaryOperation;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.runtime.handle.OperatorHandle;

/**
 * && 操作
 */
public class AndOperation extends BinaryOperation {

	public AndOperation(Expression leftOperand, Span span, Expression rightOperand) {
		super(leftOperand, span, rightOperand);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		Label end = new Label();
		compiler.visit(getLeftOperand())
				.insn(DUP)
				.invoke(INVOKESTATIC, OperatorHandle.class, "isTrue", boolean.class, Object.class)
				.jump(IFEQ, end)
				.insn(POP)
				.visit(getRightOperand())
				.label(end);
	}
}
