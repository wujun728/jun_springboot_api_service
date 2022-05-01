/*
    ...测试
*/

// ...list

var i = 0;
var fun = ()=>{
    return [1,2,3];
}
var list = [3,2,1]
var future = () => [1,2,3];
var twoDimensional = [[4,5,6]];

var r = []
r.addAll([...[1,2,3],9,6]);
r.addAll([...list,9,6]);
r.addAll([...fun(),9,6]);
r.addAll([...future(),9,6]);

// lambda调用中展开
var sum = (x,y,z)=>{
    return x + y + z;
}
r.add(sum(...[1,2,3]))

var xxx = [1,2,3]
var v = ()=>sum(...xxx);
var y = sum(...xxx)
var l = [v(),y];
r.addAll(l.map(e=>e))

r.addAll([1,2,3].map(e=>e+3))
r.addAll([...l].map(e=>e))

// ...map

var map = {key1:1,key2:2}

var map2 = {key0:0, ...map, key3:3, key4:4, ...list}
return r == [1, 2, 3, 9, 6, 3, 2, 1, 9, 6, 1, 2, 3, 9, 6, 1, 2, 3, 9, 6, 6, 6, 6, 4, 5, 6, 6, 6] && map2 == {key0:0, key1:1, key2:2, key3:3, key4:4, "0":3, "1":2, "2":1}

