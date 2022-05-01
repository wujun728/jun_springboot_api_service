var list = [{
    sex : 0,
    name : '小明'
},{
    sex : 1,
    name : '小花'
}]
return (select * from list t limit 2 offset 1).toString()
