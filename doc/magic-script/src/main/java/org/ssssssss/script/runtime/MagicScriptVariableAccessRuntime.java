package org.ssssssss.script.runtime;

import org.ssssssss.script.MagicScriptContext;

public class MagicScriptVariableAccessRuntime extends MagicScriptRuntime{

	private final String varName;

	public MagicScriptVariableAccessRuntime(String varName) {
		this.varName = varName;
	}

	@Override
	public Object execute(MagicScriptContext context) {
		return context.get(varName);
	}
}
