package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.exception.MagicExitException;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.Node;
import org.ssssssss.script.runtime.ExitValue;

import java.util.List;

public class Exit extends Node {

	private final List<Expression> expressions;

	public Exit(Span span, List<Expression> expressions) {
		super(span);
		this.expressions = expressions;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		expressions.forEach(it -> it.visitMethod(compiler));
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.typeInsn(NEW, MagicExitException.class)
				.insn(DUP)
				.typeInsn(NEW, ExitValue.class)
				.insn(DUP);
		if (expressions == null) {
			compiler.invoke(INVOKESPECIAL, ExitValue.class, "<init>", void.class);
		} else {
			compiler.newArray(expressions)
					.invoke(INVOKESPECIAL, ExitValue.class, "<init>", void.class, Object[].class);
		}
		compiler.invoke(INVOKESPECIAL, MagicExitException.class, "<init>", void.class, ExitValue.class)
				.insn(ATHROW);
	}
}
