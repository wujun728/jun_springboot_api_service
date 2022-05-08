var list1 = [{a:1,b:2,c:3}]
var list2 = [{a:1,d:4,e:5},{a:1,d:6,e:7}]
var result =
    SELECT
       x.a,
       x.b,
       x.c
    from list1 x
    left join list2 y on x.a = y.a
    group by x.a

return result.toString()
