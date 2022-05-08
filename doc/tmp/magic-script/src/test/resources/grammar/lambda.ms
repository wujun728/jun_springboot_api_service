/*
    测试Lambda  => 和 -> 作用是一样的。
*/
var lambda1 = e => e + 1; //单参数单行代码，省略括号,省略{}
var lambda2 = (e) -> e +1; //单参数单行代码，不省略括号，省略{} 作用同上
var lambda4 = e => {e + 1};//单参数无返回值，不能省略{}
var lambda5 = e => {return e + 1};//单参数有返回值，省略括号,不省略{}
var lambda6 = (e) -> {return e + 1};//单参数有返回值，不省略括号,不省略{}，作用同上
var lambda7 = (a,b) => a + b; //多参数单行代码，省略{}
var lambda7 = (a,b) -> {return a + b}; //多参数单行代码，有返回值，作用同上
var lambda8 = (a,b) =>{ //多参数多行代码， 无法省略括号和{}
    a = a + 1;
    return a + b;
}
var v1 = lambda1(1);    //返回2
var v2 = lambda2(v1);    //返回3
return lambda8(v1,lambda7(v1,v2)); //返回8
