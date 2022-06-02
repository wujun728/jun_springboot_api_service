package com.google.groovy;
// 带参数的groovy方法
def sayHello(name) {
	println "Hello " + name + "."
	
	// 如果不写return, groovy方法的默认最后一行为 方法的返回值
	//return "GroovyShell_1中的sayHello()方法的返回值"
	"GroovyShell_1中的sayHello(name)方法的返回值"
}

// 运行groovy方法
sayHello(name)