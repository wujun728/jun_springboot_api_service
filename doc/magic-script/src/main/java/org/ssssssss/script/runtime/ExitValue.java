package org.ssssssss.script.runtime;

public class ExitValue {

	private Object[] values;

	public ExitValue() {
		this(new Object[0]);
	}

	public ExitValue(Object[] values) {
		this.values = values;
	}

	public Object[] getValues() {
		return values;
	}

	public int getLength(){
		return values.length;
	}

}
