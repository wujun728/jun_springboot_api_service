package org.ssssssss.script.convert;

import org.ssssssss.script.parsing.ast.literal.BooleanLiteral;
import org.ssssssss.script.runtime.Variables;

/**
 * 任意值到boolean类型的隐式转换
 */
public class BooleanImplicitConvert implements ClassImplicitConvert {
	@Override
	public boolean support(Class<?> from, Class<?> to) {
		return to == Boolean.class || to == boolean.class;
	}

	@Override
	public Object convert(Variables variables, Object source, Class<?> target) {
		return BooleanLiteral.isTrue(source);
	}
}
