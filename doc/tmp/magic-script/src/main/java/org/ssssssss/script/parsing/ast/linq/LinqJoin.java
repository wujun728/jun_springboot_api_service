package org.ssssssss.script.parsing.ast.linq;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.runtime.function.MagicScriptLambdaFunction;
import org.ssssssss.script.runtime.linq.LinQBuilder;

public class LinqJoin extends Expression {

	private final LinqField target;

	private final Expression condition;

	private final boolean leftJoin;

	private String methodName;

	public LinqJoin(Span span, boolean leftJoin, LinqField target, Expression condition) {
		super(span);
		this.leftJoin = leftJoin;
		this.target = target;
		this.condition = condition;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		this.methodName = compiler.visitMethod("linq_join_condition", ()-> compiler.compile(condition).insn(ARETURN));
	}

	public LinqField getTarget() {
		return target;
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		compiler.load0()
				.lambda(methodName)
				.visit(target.getExpression())
				.insn(leftJoin ? ICONST_1 : ICONST_0)
				.visitInt(target.getVarIndex() == null ? -1 : target.getVarIndex().getIndex())
				.invoke(INVOKEVIRTUAL, LinQBuilder.class, "join", LinQBuilder.class, MagicScriptLambdaFunction.class, Object.class, boolean.class, int.class);
	}

}
