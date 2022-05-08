package org.ssssssss.script.parsing.ast.linq;

import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.VarIndex;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.VariableSetter;

public class LinqField extends LinqExpression implements VariableSetter {

	private final String aliasName;

	private final VarIndex varIndex;

	public LinqField(Span span, Expression expression, VarIndex alias) {
		super(span, expression);
		this.aliasName = alias != null ? alias.getName() : expression.getSpan().getText();
		this.varIndex = alias;
	}

	public VarIndex getVarIndex() {
		return varIndex;
	}

	public String getAlias() {
		return aliasName;
	}

}
