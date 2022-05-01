package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Node;

/**
 * continue语句
 */
public class Continue extends Node {

	public Continue(Span span) {
		super(span);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.start();
	}
}