package org.ssssssss.script.parsing.ast;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;

/**
 * 常量
 */
public abstract class Literal extends Expression {

	protected Object value = null;

	public Literal(Span span) {
		super(span);
	}

	public Literal(Span span, Object value) {
		super(span);
		this.value = value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.ldc(value);
	}
}
