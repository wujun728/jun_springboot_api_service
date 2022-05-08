package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.MagicScriptError;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

/**
 * int常量
 */
public class IntegerLiteral extends Literal {

	public IntegerLiteral(Span literal) {
		super(literal);
		try {
			setValue(Integer.parseInt(literal.getText().replace("_","")));
		} catch (NumberFormatException e) {
			MagicScriptError.error("定义int变量值不合法", literal, e);
		}
	}

	public IntegerLiteral(Span span, Object value) {
		super(span, value);
	}

	@Override
	public void compile(MagicScriptCompiler context) {
		context.visitInt((Integer) value)
				.invoke(INVOKESTATIC, Integer.class, "valueOf", Integer.class, int.class);
	}
}