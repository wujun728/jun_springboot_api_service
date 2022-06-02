package com.soukuan.demo1;

public class Person implements Say{
	
	private String name;
	
	public Person() {
		super();
	}

	public Person(String name) {
		super();
		this.name = name;
	}

	@Override
	public void sayHello() {
		System.out.println("My name is "+name+"!");
		throw new RuntimeException();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}