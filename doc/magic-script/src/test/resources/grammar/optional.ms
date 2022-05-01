var v1 = a?.b.c();
var v2 = v1?.xx();
var v3 = v2?.abcd;
return "" + (v1 == null) + (v2 == null) + (v3 == null);