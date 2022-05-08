package org.ssssssss.script.functions;

import java.util.Arrays;

public class Role {

	private String name;

	private String[] permissions;

	public String getName() {
		return name;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role{" +
				"name='" + name + '\'' +
				", permissions=" + Arrays.toString(permissions) +
				'}';
	}
}
