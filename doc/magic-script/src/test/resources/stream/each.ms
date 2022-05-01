var list;
list = [{a:1}];
return list.each(it => {
    it.a = it.a == 1 ? 'yes' : 'no'
})