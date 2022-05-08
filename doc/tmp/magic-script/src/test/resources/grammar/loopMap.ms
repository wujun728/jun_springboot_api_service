/*
    测试循环Map
*/
var k = 'key4';
var k5 = 5;
var map = {
    key1 : 1,
    key2 : 2,
    key3 : 3,
    [k]: 4,
    k5
};
var sum = 0;
var keys = '';
for(key,value in map){
    sum = sum + value;
    keys = keys + key
}
return keys + '-' + sum;