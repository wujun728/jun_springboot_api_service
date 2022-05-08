var x = 1;
try{
    x = x / 0;
} catch(e){
    x = 3;
} finally{
  x = 2;
}
return x;