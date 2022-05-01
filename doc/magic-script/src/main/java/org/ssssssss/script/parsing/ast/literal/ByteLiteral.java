package org.ssssssss.script.parsing.ast.literal;

import org.ssssssss.script.MagicScriptError;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Literal;

/**
 * byte常量
 */
public class ByteLiteral extends Literal {

	private byte value;

	public ByteLiteral(Span literal) {
		super(literal);
		try {
			this.value = Byte.parseByte(literal.getText().substring(0, literal.getText().length() - 1).replace("_",""));
		} catch (NumberFormatException e) {
			MagicScriptError.error("定义byte变量值不合法", literal, e);
		}
	}

	public ByteLiteral(Span span, Object value) {
		super(span, value);
		this.value = ((Number) value).byteValue();
	}

	@Override
	public void compile(MagicScriptCompiler context) {
		context.bipush(this.value).invoke(INVOKESTATIC, Byte.class, "valueOf", Byte.class, byte.class);
	}
}