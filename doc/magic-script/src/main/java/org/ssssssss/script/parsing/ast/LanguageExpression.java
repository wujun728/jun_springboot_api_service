package org.ssssssss.script.parsing.ast;

import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.runtime.function.MagicScriptLanguageFunction;


public class LanguageExpression extends Expression {

	private final String language;

	private final String content;

	public LanguageExpression(Span language, Span content) {
		super(new Span(language, content));
		this.language = language.getText();
		this.content = content.getText();
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		// new MagicScriptLanguageFunction(language, content)
		compiler.typeInsn(NEW, MagicScriptLanguageFunction.class)
				.insn(DUP)
				.loadContext()
				.ldc(this.language)
				.ldc(this.content)
				.invoke(INVOKESPECIAL, MagicScriptLanguageFunction.class, "<init>", void.class, MagicScriptContext.class, String.class, String.class);
	}
}
