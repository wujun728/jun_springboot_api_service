var p1 = {a:1,b:2,c:3};
var t1 = {};
p1.keySet().map(rr => {
    var item_ = t1[rr];
    if(!item_){
        item_ = {c1:rr};
        t1[rr] = item_;
    }
})
return t1.toString();