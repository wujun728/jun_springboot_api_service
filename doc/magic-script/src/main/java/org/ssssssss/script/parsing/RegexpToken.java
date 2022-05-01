package org.ssssssss.script.parsing;

public class RegexpToken extends Token {

	private int flag;

	public RegexpToken(TokenType type, Span span, int regFlag) {
		super(type, span);
		this.flag = regFlag;
	}

	public int getFlag() {
		return flag;
	}
}
