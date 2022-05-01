// https://gitee.com/ssssssss-team/magic-api/issues/I29LQG
var test = (obj) => {
    System.out.println(obj);
    var x = '';
    return eval('obj.name') + eval('x');
}
return test({name : 'xxx'}) == 'xxx'