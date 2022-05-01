package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.MagicScriptError;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

/**
 * double常量
 */
public class DoubleLiteral extends Literal {

	public DoubleLiteral(Span literal) {
		super(literal);
		try {
			setValue(Double.parseDouble(literal.getText().replace("_","")));
		} catch (NumberFormatException e) {
			MagicScriptError.error("定义double变量值不合法", literal, e);
		}
	}

	@Override
	public void compile(MagicScriptCompiler context) {
		context.ldc(value).invoke(INVOKESTATIC, Double.class, "valueOf", Double.class, double.class);
	}
}