package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.Literal;
import org.ssssssss.script.parsing.ast.statement.Spread;

import java.util.List;

/**
 * List常量
 */
public class ListLiteral extends Literal {

	public final List<Expression> values;

	public ListLiteral(Span span, List<Expression> values) {
		super(span);
		this.values = values;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		values.forEach(expr -> expr.visitMethod(compiler));
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		int size = values.size();
		if (size == 0) {
			compiler.newArrayList();
		} else {
			compiler.insn(values.stream().anyMatch(it -> it instanceof Spread) ? ICONST_1 : ICONST_0)
					.asBoolean()
					.newArray(values)
					.call("newArrayList", 2);
		}
	}
}