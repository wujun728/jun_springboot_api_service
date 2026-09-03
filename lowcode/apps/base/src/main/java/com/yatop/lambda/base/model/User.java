package com.yatop.lambda.base.model;

import com.yatop.lambda.base.model.jwt.JWTUtil;
import com.yatop.lambda.base.model.jwt.PortalUtil;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.*;
import java.util.stream.Collectors;



@Table(name = "base_user", displayName = "用户")
public class User extends Model<User> {
    @Id
    public Long id;

    @Column(label = "用户名")
    private String userName;

    @Column(label = "密码")
    private String password;

    @Column(label = "昵称")
    private String nickName;


    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;


    @Column(label = "状态")
    private Boolean status;


    @ManyToMany(mappedBy = "userList")
    private List<Role> roleList;

    public User setId(Long id) {
        this.set("id", id);
        return this;
    }

    public User setUserName(String userName) {
        this.set("userName", userName);
        return this;
    }

    public String getPassword() {
        return (String) this.get("password");
    }

    public User setPassword(String password) {
        this.set("password", password);
        return this;
    }

    public String getNickName() {
        return (String) this.get("nickName");
    }

    public User setNickName(String nickName) {
        this.set("nickName", nickName);
        return this;
    }

    public User setLoginType(String loginType) {
        this.set("loginType", loginType);
        return this;
    }

    public Long getId(){
        return getLong("id");
    }


    public String getUserName(){
        return getStr("userName");
    }

    public String getLoginType(){
        return getStr("loginType");
    }




    @Service(displayName = "登录")
    public Map<String,Object> login(String login, String password){
        Map<String,Object> result = new HashMap<>();

        //TODO LOGIN
        User user =  this.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName,login));
        if(null == user){
            return Collections.EMPTY_MAP;
        }

        //TODO SIGN TOKEN
        String sign = JWTUtil.sign(login, user.getId(), true, password);
        String token = PortalUtil.encryptToken(sign);
        result.put("token", token);
        result.put("user", user);

        return result;
    }




    @Service(displayName = "获取用户")
    public Map<String,Object> getUserId(String token){
        Map<String,Object> user = PortalUtil.getUser(token);
        return user;
    }






    @Service
    public Map<String,Object> getUserInfo(String token){
        Map<String,Object> result = new HashMap<>();

        Map<String,Object> user = new HashMap<>();
        user.put("nickName","dd");
        user.put("avatar","");

        result.put("roles", Arrays.asList("admin"));
        result.put("permissions", Arrays.asList("*:*:*"));
        result.put("user", user);

        return result;
    }


}
