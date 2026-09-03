package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.CascadeType;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.List;
import java.util.stream.Collectors;

@Table(name = "im_group_user", displayName = "群组用户")
public class GroupUser extends Model<GroupUser> {
    @Id
    private Long id;
    @Column(name = "group_nick_name", label = "群组昵称")
    private String groupNickName;

    @Column(label = "用户id")
    private Long userId;

    @ManyToOne(cascade = CascadeType.DELETE)
    @JoinColumn(name = "group_id")
    private Group groupId;


    @Service
    public List<Long> myGroupList(){
        LambdaQueryWrapper<GroupUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(GroupUser::getUserId, Context.getInstance().getUserId());
        List<GroupUser> groupUserList = this.search(lambdaQueryWrapper, 0, 0, null);
        List<Long> list = groupUserList.stream().map(GroupUser::getGroupId).collect(Collectors.toList());
        return  list;
    }


    //TODO

    public Long getId() {
        return (Long) this.get("id");
    }

    public GroupUser setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getGroupNickName() {
        return (String) this.get("groupNickName");
    }

    public GroupUser setGroupNickName(String groupNickName) {
        this.set("groupNickName", groupNickName);
        return this;
    }

    public Long getUserId() {
        return (Long) this.get("userId");
    }

    public GroupUser setUserId(Long userId) {
        this.set("userId", userId);
        return this;
    }

    public Long getGroupId() {
        return (Long) this.get("groupId");
    }

    public GroupUser setGroupId(Group groupId) {
        this.set("groupId", groupId);
        return this;
    }
}
