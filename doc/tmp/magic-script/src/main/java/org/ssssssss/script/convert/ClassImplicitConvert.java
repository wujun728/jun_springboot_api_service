package org.ssssssss.script.convert;

import org.ssssssss.script.runtime.Variables;

public interface ClassImplicitConvert {

	/**
	 * 转换顺序
	 */
	default int sort() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 是否支持隐式自动转换
	 */
	boolean support(Class<?> from, Class<?> to);

	/**
	 * 转换
	 */
	Object convert(Variables variables, Object source, Class<?> target);
}
