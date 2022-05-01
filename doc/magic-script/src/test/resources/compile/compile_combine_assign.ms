var a = 1;
var b = a = a + 1;
while((a = a + 1) < 5){
 println(a)
}
return a;