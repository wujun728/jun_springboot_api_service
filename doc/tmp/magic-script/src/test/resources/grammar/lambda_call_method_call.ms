var f = ()=>[1,2,3,4,5];
return f()[0] + [1,2,3].map(e=>e+1)[0].toString() + 666;