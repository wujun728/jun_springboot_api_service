
var a = /abc.*?f/gis
var b = /abc.*?f/gi
var s = """
abc
DEF
""";
var arr = [
// list注释
3,2,1,/matcher1/g
]
var map = {
// map 注释
a:1,
v:a,
x:/matcher2/gi
}

var regx = /\//g;
System.out.println(regx.test("/"))


System.out.println(/x/.test('x'))

System.out.println(arr)
System.out.println(map)

System.out.println(s.class)
System.out.println(a)
System.out.println(a.matcher(s).find())
System.out.println(b.matcher(s).find())

System.out.println(b.test(s))
System.out.println(s.match(b))


System.out.println(/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/.test('123456@qq.com'))