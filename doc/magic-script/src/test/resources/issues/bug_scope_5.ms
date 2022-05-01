var func = ()=>{
    var obj = {
        v1:1,
        save: ()=> {
            return obj.v1
        }
    }
    return obj;
}
return func().save();