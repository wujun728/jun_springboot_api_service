var map = {
    key1 : 1,
    key2 : 2,
    key3 : 3
};
var sum = 0;
var keys = '';
map.each((key,value) => {
    keys += key;
    sum += value;

})
return keys + '-' + sum;