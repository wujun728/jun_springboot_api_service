var func1 = (index)=>{
    var f = async index=>{
        return index + 1;
    }
    return f.get();
}
var list = []
for(index in range(-1,98)){
    list.add(async (index)=> func1(index + 1))
}
return list.map(it => it.get()).sum();