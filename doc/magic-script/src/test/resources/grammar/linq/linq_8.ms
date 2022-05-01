var listAA = [
    {id: 0, name: 'AA_0'},
    {id: 1000, name: 'AA_1'},
    {id: 2, name: 'AA_2'},
    {id: 3000, name: 'AA_3_1'},
    {id: 3000, name: 'AA_3_2'},
]
var listA = [
    {id: 1, name: 'A_1', aaid: 1},
    {id: 1, name: 'A_1', aaid: 1},
    {id: 2, name: 'A_2', aaid: 2},
    {id: 2, name: 'A_2', aaid: 2},
    {id: 3, name: 'A_3', aaid: 3},
    {id: 3, name: 'A_3', aaid: 3}
]

var listB = [
    {id: 1, name: 'B_1', aid: 1},
    {id: 1, name: 'B_1', aid: 1},
    {id: 2, name: 'B_2', aid: 2},
    {id: 2, name: 'B_2', aid: 2},
    {id: 3, name: 'B_3', aid: 3},
    {id: 3, name: 'B_3', aid: 3},
    {id: 10, name: 'B_10', aid: 1},
    {id: 20, name: 'B_20', aid: 2},
    {id: 30, name: 'B_30', aid: 3}
]

var listC = [
    { name: "C_1", bid:1 },
    { name: "C_2", bid:2 },
    { name: "C_3", bid:3 },
    { name: "C_10", bid:10 },
    { name: "C_20", bid:20 },
    { name: "C_30", bid:30 },
    { name: "C_40", bid:40 }
]

const result =
    select
        b.name bname,
        c.name cname
    from listC c
    left join listB b on b.id = c.bid

result.each(row => println(row))
return result.toString()
