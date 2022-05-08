package org.ssssssss.script.runtime;

import org.ssssssss.script.MagicScriptContext;

import java.util.Map;

public class RuntimeContext {

	private final MagicScriptContext context;

	private Variables variables;

	public RuntimeContext(MagicScriptContext context, Variables variables) {
		this.context = context;
		this.variables = variables;
	}

	public Variables getVariables() {
		return variables;
	}

	public Map<String, Object> getVarMap() {
		return variables.getVariables(context);
	}

	public MagicScriptContext getScriptContext() {
		return context;
	}

	public Object eval(String script){
		return this.context.eval(this, script);
	}
}
