var item = {val : 123}
var func = (item)=>{
    var data = {};
    var r1 = async ()=>item.val;
    return r1.get();
}
return func(item);