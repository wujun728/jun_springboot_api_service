var a = 123;
var b = "hello";
try{
 var c = a / 0;
}catch(e){
    exit 200,'success';
}
exit 200,'error';