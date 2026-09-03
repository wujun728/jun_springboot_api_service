package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.List;

@Table(name = "im_friend", displayName = "好友")
public class Friend extends Model<Friend> {
    @Id
    private Long id;

    @Column(label = "用户id")
    private Long userId;

    @Column(label = "好友id")
    private Long friendId;

    @Column(name = "user_remark")
    private String userRemark;


    @Service
    public List<Friend> myFriends(){
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Friend::getUserId, Context.getInstance().getUserId());
        return this.search(lambdaQueryWrapper, 0, 0, null);
    }

    public Long getId() {
        return (Long) this.get("id");
    }

    public Friend setId(Long id) {
        this.set("id", id);
        return this;
    }

    public Long getUserId() {
        return (Long) this.get("userId");
    }

    public Friend setUserId(Long userId) {
        this.set("userId", userId);
        return this;
    }

    public Long getFriendId() {
        return (Long) this.get("friendId");
    }

    public Friend setFriendId(Long friendId) {
        this.set("friendId", friendId);
        return this;
    }

    public String getUserRemark() {
        return (String) this.get("userRemark");
    }

    public Friend setUserRemark(String userRemark) {
        this.set("userRemark", userRemark);
        return this;
    }
}
