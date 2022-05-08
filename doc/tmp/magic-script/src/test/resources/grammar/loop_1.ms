var sum = 0;
for(val in range(0,100)){   //包括0 包括100
    if(val > 90){
        break;  //跳出循环
    }
    if(val % 3 == 0){
        continue;   //进入下一次循环
    }
    sum = sum + val;
}
return sum;