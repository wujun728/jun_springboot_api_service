package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

import java.math.BigDecimal;

/**
 * int常量
 */
public class BigDecimalLiteral extends Literal {
	public BigDecimalLiteral(Span literal) {
		super(literal, literal.getText().substring(0, literal.getText().length() - 1).replace("_",""));
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.typeInsn(NEW, BigDecimal.class)
				.insn(DUP)
				.ldc(value)
				.invoke(INVOKESPECIAL, BigDecimal.class, "<init>", void.class, String.class);
	}
}