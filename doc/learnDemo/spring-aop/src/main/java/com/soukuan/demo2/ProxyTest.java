package com.soukuan.demo2;

import com.soukuan.demo1.Animals;
import com.soukuan.demo1.Person;
import com.soukuan.demo1.Say;

public class ProxyTest {

	public static void main(String[] args){
		cglibTest();
	}
	
	public static void cglibTest(){
		Say p=new Person("lg");
		p=CglibProxy.createProxy(p);
		p.sayHello();
		
		System.out.println("-------------------------------");

		Say animals=new Animals();
		animals=CglibProxy.createProxy(animals);
		animals.sayHello();
	}
}