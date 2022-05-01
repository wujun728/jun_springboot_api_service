package org.ssssssss.script.parsing.ast;

import org.ssssssss.script.parsing.Span;

/**
 * 表达式
 */
public abstract class Expression extends Node {
	public Expression(Span span) {
		super(span);
	}

}