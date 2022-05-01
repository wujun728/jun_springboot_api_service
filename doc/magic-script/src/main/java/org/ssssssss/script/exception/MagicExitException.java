package org.ssssssss.script.exception;

import org.ssssssss.script.runtime.ExitValue;

public class MagicExitException extends RuntimeException {

	private final ExitValue exitValue;

	public MagicExitException(ExitValue exitValue) {
		this.exitValue = exitValue;
	}

	public ExitValue getExitValue() {
		return exitValue;
	}
}
