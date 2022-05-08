package org.ssssssss.script.runtime.function;

import org.ssssssss.script.runtime.Variables;

@FunctionalInterface
public interface MagicScriptLambdaFunction {

	Object apply(Variables variables, Object[] args);
}
