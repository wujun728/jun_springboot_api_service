package com.soukuan.demo1;


public class ProxyTest {

	public static void main(String[] args){
		Say say1=new Person("lg");
		say1=(Say)JDKDynamicProxy.createProxy(say1);
		say1.sayHello();
		
		System.out.println("-------------------------------");
		
		Say say3=new Animals();
		say3=(Say) JDKDynamicProxy.createProxy(say3);
		say3.sayHello();
	}
}