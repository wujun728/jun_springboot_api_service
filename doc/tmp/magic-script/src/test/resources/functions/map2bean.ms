import 'org.ssssssss.script.functions.User' as User;
var user = {
    age : 18,
    weight : 121,
    money : 123456789L,
    name : '法外狂徒',
    roles : [{
        name : 'admin',
        permissions : ['1','2','3']
    },{
      name : 'normal',
      permissions : ['4','5','6']
    }]
}
return user.asBean(User.class);