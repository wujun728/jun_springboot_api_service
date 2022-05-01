var user = {
    username: 123
};
var func1 = () => {
    var user = 666;
    return user.username;
}
var func = ()=> user.username;
return func();