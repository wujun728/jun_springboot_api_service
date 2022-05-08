var n = 111;
var f = (n)=>{
    n = n + 0;
    if(n <= 2){
        return 1;
    }
    return f(n - 1) + f(n - 2);
}
return f(10)