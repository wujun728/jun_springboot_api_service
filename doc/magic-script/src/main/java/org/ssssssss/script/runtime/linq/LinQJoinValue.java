package org.ssssssss.script.runtime.linq;

import org.ssssssss.script.runtime.function.MagicScriptLambdaFunction;

import java.util.List;

public class LinQJoinValue {

	private final MagicScriptLambdaFunction condition;

	private final List<Object> target;

	private final boolean isLeftJoin;

	private int aliasIndex = -1;

	public LinQJoinValue(MagicScriptLambdaFunction condition, List<Object> objects, boolean isLeftJoin, int aliasIndex) {
		this.condition = condition;
		this.target = objects;
		this.isLeftJoin = isLeftJoin;
		this.aliasIndex = aliasIndex;
	}

	public MagicScriptLambdaFunction getCondition() {
		return condition;
	}

	public int size(){
		return target.size();
	}

	public List<Object> getTarget() {
		return target;
	}

	public boolean isLeftJoin() {
		return isLeftJoin;
	}

	public int getAliasIndex() {
		return aliasIndex;
	}
}
