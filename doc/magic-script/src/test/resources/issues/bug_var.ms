var sum = (a,b,c)=>a+b+c;
var f = async sum(1,2,3);
//f.get();
f = async sum(4,5,6);
return f.get()