var a = 1;
var b = "123";
var c = 456L;
var d = true;
var e = null;
e = c;
a = b;
c = false;
var d = [1,2,3,4,5,a,b,c,d,e,false];
var map = {
    k1: 123,
    k2: "456",
    [b]: false,
}
map.k1 = 555;
map.k1 = 999;
return a + c + b +d[10];