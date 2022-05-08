package org.ssssssss.script.exception;

import org.ssssssss.script.parsing.Span;

public class MagicScriptException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String simpleMessage;
	private Span location;

	public MagicScriptException(String errorMessage, String simpleMessage, Span location) {
		super(errorMessage);
		this.simpleMessage = simpleMessage;
		this.location = location;
	}

	public MagicScriptException(String errorMessage, Span location) {
		this(errorMessage, errorMessage, location);
	}

	public MagicScriptException(String errorMessage) {
		this(errorMessage, errorMessage, null);
	}

	public MagicScriptException(String message, String simpleMessage, Throwable cause, Span location) {
		super(message, cause);
		this.simpleMessage = simpleMessage;
		this.location = location;
	}

	public String getSimpleMessage() {
		return simpleMessage;
	}

	public Span getLocation() {
		return location;
	}

	public Span.Line getLine() {
		return location == null ? null : location.getLine();
	}
}