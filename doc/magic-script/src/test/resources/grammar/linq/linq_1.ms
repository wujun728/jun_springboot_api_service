var result =
    select
       x.a,
       [t] value,
       x.b,
       count(t.a) count,
       min(x.b),
       max(x.b),
       avg(x.b),
       date_format(new Date()) format_value
    from [{a:1,b:2,c:3},{a:2,b:5,c:6}] x
    join [{xxx : 6}] t on 1=1
    group by x.a
    order by x.b * 50 desc

result.each(it => System.out.println(it))
