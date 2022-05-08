package org.ssssssss.script.parsing.ast.linq;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

public class WholeLiteral extends Literal {

	public WholeLiteral(Span span) {
		super(span);
	}

	public WholeLiteral(Span span, Object value) {
		super(span, value);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.load2();
	}
}
