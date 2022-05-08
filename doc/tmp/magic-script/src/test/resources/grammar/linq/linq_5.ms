var list = [{
    sex : 0,
    name : '小明'
},{
    sex : 1,
    name : '小花'
}]
return (select * from list t where t.sex = 0 limit 1).toString()
