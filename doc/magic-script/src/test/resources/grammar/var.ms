/*
    测试变量定义
*/
var int = 1;
var double = 2.0d; //2.0D
var long =  3L; // 3l
var float =  4f; // 4F
var byte =  5b; // 5B;
var short = 6s; //6S
var boolean = true; //true or false;
var nullValue = null; // null
var list = [1,2,3,4,5]; //定义list
var map = {
    k1 : 123,
    k2 : "456",
    k3 : 789L,
    k4 : {
        k5 : '...'
    }
};  //定义map
var string1 = "str";    //定义字符串
var string2 = 'str2'; // \t \n \r \' \" \\ 转义符是支持的，其它的不支持
//文本块，主要用于定义SQL
var string3 = """
    select
        *
    from table t1
    join table2 t2 on t2.xx = t1.xx
    where t1.id in (1,2,3,4,5,6)
"""

var lambda = e=>e+1;    //定义lambda