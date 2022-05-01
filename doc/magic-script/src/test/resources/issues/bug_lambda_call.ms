var f = a => a;
var f1 = (a,b) => f();
return f1(1);