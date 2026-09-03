package com.yatop.lambda.im.model;

import com.alibaba.fastjson.JSONObject;
import com.yatop.lambda.im.constant.ConversationType;
import com.yatop.lambda.im.constant.MessageStatus;
import com.yatop.lambda.im.constant.MessageType;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
@APP(displayName = "通讯服务", name = "im", depends = {"net","base"})
@Table(name = "im_group", displayName = "群组")
public class Group extends Model<Group> {
    @Id
    private Long id;
    @Column(label = "群组名称")
    private String name;
    @File(label = "群组头像")
    private String avatarUrl;
    @Column(label = "群主用户ID")
    private Long leaderUserId;
    @Column(label = "加群方式")
    private Integer joinMode;
    @Column(label = "群介绍")
    private String introduction;
    @Column(label = "群公告")
    private String notification;
    @Dict(typeCode = "mute", label = "全体禁言")
    private String isMute;
    @Dict(typeCode = "dissolve", label = "解散群组")
    private String isDissolve;
    @Column(label = "创建时间")
    private Date createdAt;


    @Service
    public boolean create(Group value) {
        value.save();

        GroupUser groupUser = new GroupUser();
        groupUser.setUserId(Context.getInstance().getUserId());
        groupUser.set("groupId", value.getId());
        groupUser.save();


        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setType(MessageType.GROUP_SYS_NOTICE);
        groupMessage.setConversationType(ConversationType.GROUP);
        groupMessage.setConversationId(value.getId());
        groupMessage.setFrom(0L);
        JSONObject bj = new JSONObject(new HashMap<String, Object>() {{
            put("tips", Context.getInstance().getUserId() + "创建了群组：" + value.getName());
        }});
        groupMessage.setBody(bj.toJSONString());
        groupMessage.setIsRead(0);
        groupMessage.setDirection("out");
        groupMessage.setIsRevoke(0);
        groupMessage.setTime(System.currentTimeMillis());
        groupMessage.setStatus( MessageStatus.SUCCESS);
        groupMessage.setTo(value.getId());

        //
        Context.getInstance().get("im.im_group_message").create(groupMessage);

        return true;
    }




    @Service
    public List<Group> myGroupList(){
        LambdaQueryWrapper<Group> lambdaQueryWrapper = Wrappers.lambdaQuery();

        List<Group> groupList  = this.search(lambdaQueryWrapper, 0, 0, null);
        return groupList;
    }


    public Long getId() {
        return (Long) this.get("id");
    }

    public Group setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public Group setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getAvatarUrl() {
        return (String) this.get("avatarUrl");
    }

    public Group setAvatarUrl(String avatarUrl) {
        this.set("avatarUrl", avatarUrl);
        return this;
    }

    public Long getLeaderUserId() {
        return (Long) this.get("leaderUserId");
    }

    public Group setLeaderUserId(Long leaderUserId) {
        this.set("leaderUserId", leaderUserId);
        return this;
    }

    public Integer getJoinMode() {
        return (Integer) this.get("joinMode");
    }

    public Group setJoinMode(Integer joinMode) {
        this.set("joinMode", joinMode);
        return this;
    }

    public String getIntroduction() {
        return (String) this.get("introduction");
    }

    public Group setIntroduction(String introduction) {
        this.set("introduction", introduction);
        return this;
    }

    public String getNotification() {
        return (String) this.get("notification");
    }

    public Group setNotification(String notification) {
        this.set("notification", notification);
        return this;
    }

    public String getIsMute() {
        return (String) this.get("isMute");
    }

    public Group setIsMute(String isMute) {
        this.set("isMute", isMute);
        return this;
    }

    public String getIsDissolve() {
        return (String) this.get("isDissolve");
    }

    public Group setIsDissolve(String isDissolve) {
        this.set("isDissolve", isDissolve);
        return this;
    }

    public Date getCreatedAt() {
        return (Date) this.get("createdAt");
    }

    public Group setCreatedAt(Date createdAt) {
        this.set("createdAt", createdAt);
        return this;
    }
}
