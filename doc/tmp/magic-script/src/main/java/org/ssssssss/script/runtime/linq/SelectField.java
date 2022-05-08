package org.ssssssss.script.runtime.linq;

import org.ssssssss.script.runtime.function.MagicScriptLambdaFunction;

public class SelectField {

	private final MagicScriptLambdaFunction function;

	private final String aliasName;

	private final int aliasIndex;

	public SelectField(MagicScriptLambdaFunction function, String aliasName, int aliasIndex) {
		this.function = function;
		this.aliasName = aliasName;
		this.aliasIndex = aliasIndex;
	}

	public MagicScriptLambdaFunction getFunction() {
		return function;
	}

	public String getAliasName() {
		return aliasName;
	}

	public boolean isWhole(){
		return aliasIndex == -1 && "*".equals(aliasName);
	}
}
