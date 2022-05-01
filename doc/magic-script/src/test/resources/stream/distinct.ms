var list = [1,2,3,1,2,5,6];
return list.distinct().toString() + list.distinct(e=>e%3==0?null:e)
