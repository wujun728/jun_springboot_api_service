var list1 = [{
    id: 1,
    name: 'A1'
},{
    id: 2,
    name: 'A2'
}]
var list2 = [{
    aid: 1,
    name: 'B1',
    id: 10
},{
    aid: 2,
    name: 'B2',
    id: 20
},{
    aid: 2,
    name: 'B22',
    id: 21
}]
var list3 = [{
    bid: 10,
    name: 'C1'
},{
    bid: 21,
    name: 'C22'
}]
return
    (select
        a.name aname,
        b.name bname,
        c.name cname
    from list1 a
    left join list2 b on b.aid = a.id
    join list3 c on c.bid = b.id
    ).toString()
