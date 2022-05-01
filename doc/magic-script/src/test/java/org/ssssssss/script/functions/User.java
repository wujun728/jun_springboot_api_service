package org.ssssssss.script.functions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class User {

	private int age;

	private double weight;

	private long money;

	private List<Role> roles;

	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User{" +
				"age=" + age +
				", weight=" + weight +
				", money=" + money +
				", roles=" + roles +
				", name='" + name + '\'' +
				'}';
	}
}
