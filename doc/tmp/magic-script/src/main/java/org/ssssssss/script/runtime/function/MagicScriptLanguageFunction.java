package org.ssssssss.script.runtime.function;

import org.ssssssss.script.MagicResourceLoader;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.runtime.Variables;

import java.util.Map;
import java.util.function.BiFunction;

public class MagicScriptLanguageFunction implements MagicScriptLambdaFunction{

	private final BiFunction<Map<String, Object>, String, Object> function;

	private final String content;

	private final MagicScriptContext context;

	public MagicScriptLanguageFunction(MagicScriptContext context, String language, String content) {
		this.context = context;
		this.function = MagicResourceLoader.loadScriptLanguage(language);
		this.content = content;
	}

	@Override
	public Object apply(Variables variables, Object[] args) {
		Map<String, Object> vars = variables.getVariables(this.context);
		return function.apply(vars, this.content);
	}
}
