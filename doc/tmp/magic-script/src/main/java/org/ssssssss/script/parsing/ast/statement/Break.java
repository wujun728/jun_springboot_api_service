package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Node;

/**
 * break 语句
 */
public class Break extends Node {

	public Break(Span span) {
		super(span);
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.end();
	}
}