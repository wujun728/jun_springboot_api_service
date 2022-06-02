package com.google.groovy;
// 不带参数的groovy方法
def sayHello() {
	println 'Hello World.'
	
	// 如果不写return, groovy方法的默认最后一行为 方法的返回值
	//return "GroovyShell_1中的sayHello()方法的返回值"
	"GroovyShell_1中的sayHello()方法的返回值"
}

// 运行groovy方法
sayHello()