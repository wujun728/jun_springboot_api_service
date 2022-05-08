package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

/**
 * null 常量
 */
public class NullLiteral extends Literal {
	public NullLiteral(Span span) {
		super(span);
	}

	@Override
	public void compile(MagicScriptCompiler context) {
		context.insn(ACONST_NULL);
	}
}