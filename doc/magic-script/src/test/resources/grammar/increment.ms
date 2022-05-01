var a = { index : 10 };
var b = 10;
var c = a.index++ + ++a.index;
var d = b-- - --b;
return [a,b,c,d];