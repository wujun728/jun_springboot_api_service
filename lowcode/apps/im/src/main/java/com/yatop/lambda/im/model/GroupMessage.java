package com.yatop.lambda.im.model;

import com.yatop.lambda.im.constant.ConversationType;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.IdWorker;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.List;

@Table(name = "im_group_message", displayName = "群组消息")
public class GroupMessage extends Model<GroupMessage> {
    @Id
    private Long id;

    @Column(label = "消息 id")
    private Long messageId;

    /**
     * 消息所属用户
     */
    @Column(label = "用户id")
    private Long userId;


    /**
     * 消息所属会话ID
     */
    @Column(label = "会话 id")
    private Long conversationId;

    /**
     * 会话类型
     */
    @Column(label = "会话类型")
    private String conversationType;

    /**
     * 消息方向：in=接收 out等于发出
     */
    @Column(label = "消息方向")
    private String direction;

    /**
     * 消息发送方
     */
    @Column(label = "消息发送方")
    private Long from;


    @Column(label = "来自用户")
    private Long fromUserInfo;

    /**
     * 消息接收方
     */
    @Column(label = "消息接收方")
    private Long to;

    /**
     * 消息类型
     */
    @Column(label = "消息类型")
    private String type;

    /**
     * 消息内容
     */
    @Column(label = "消息内容")
    private String body;

    /**
     * 扩展的自定义数据(字符串类型)
     */
    @Column(label = "扩展的自定义数据")
    private String extra;

    /**
     * 对方是否已读
     */
    @Column(label = "对方是否已读")
    private Integer isRead;

    /**
     * 是否被撤回的消息
     */
    @Column(label = "是否被撤回的消息")
    private Integer isRevoke;

    /**
     * 是否被删除的消息
     */
    @Column(label = "是否被删除的消息")
    private Integer isDeleted;

    /**
     * 消息状态：
     * unSend(未发送)
     * success(发送成功)
     * fail(发送失败)
     */
    @Column(label = "消息状态")
    private String status;

    /**
     * 对方接收状态
     * 0 = 未接收
     * 1 = 已接收
     */
    @Column(label = "对方接收状态")
    private Integer receive;

    /**
     * 消息时间，毫秒级时间戳
     */
    @Column(label = "消息时间")
    private Long time;



    @Service
    public boolean create(GroupMessage value){
        value.setMessageId(IdWorker.getId());
        value.save();


       Group group =  new Group().selectOne(Criteria.equal("id", value.getConversationId()));

       LambdaQueryWrapper<GroupUser> groupUserLambdaQueryWrapper = Wrappers.lambdaQuery();
       List<GroupUser> groupUserList = new GroupUser().search(groupUserLambdaQueryWrapper.eq(GroupUser::getGroupId, group.getId()), 0, 0, null);

       for(GroupUser groupUser: groupUserList){

           Conversation conversation = new Conversation();
           conversation.setConversationId(group.getId());
           conversation.setType(ConversationType.GROUP);
           conversation.setLastMessageId(value.getMessageId());
           conversation.setUserId(groupUser.getUserId());
           conversation.setName(group.getName());
           conversation.setLastMessage(value.getBody());
           int unread = 0;
           //更新群成员会话，并发送会话更新事件（已删除的成会话仍在，但非群成员则会话和消息均不更新）
           if (value.getFrom() != groupUser.getUserId()) {
               unread = 1;
           }
           conversation.setUnread(unread);
           Context.getInstance().get("im.im_conversation").call("updateConversation", conversation);
       }

        return true;
    }


    //TODO 服务
    @Service
    public List<GroupMessage> myHistoryMessage(Long conversationId){
        LambdaQueryWrapper<GroupMessage> lambdaQueryWrapper  = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(GroupMessage::getConversationId, conversationId);
       return  this.search(lambdaQueryWrapper, 0, 0, null);
    }



    public Long getId() {
        return (Long) this.get("id");
    }

    public GroupMessage setId(Long id) {
        this.set("id", id);
        return this;
    }

    public Long getMessageId() {
        return (Long) this.get("messageId");
    }

    public GroupMessage setMessageId(Long messageId) {
        this.set("messageId", messageId);
        return this;
    }

    public Long getUserId() {
        return (Long) this.get("userId");
    }

    public GroupMessage setUserId(Long userId) {
        this.set("userId", userId);
        return this;
    }

    public Long getConversationId() {
        return (Long) this.get("conversationId");
    }

    public GroupMessage setConversationId(Long conversationId) {
        this.set("conversationId", conversationId);
        return this;
    }

    public String getConversationType() {
        return (String) this.get("conversationType");
    }

    public GroupMessage setConversationType(String conversationType) {
        this.set("conversationType", conversationType);
        return this;
    }

    public String getDirection() {
        return (String) this.get("direction");
    }

    public GroupMessage setDirection(String direction) {
        this.set("direction", direction);
        return this;
    }

    public Long getFrom() {
        return (Long) this.get("from");
    }

    public GroupMessage setFrom(Long from) {
        this.set("from", from);
        return this;
    }

    public Long getFromUserInfo() {
        return (Long) this.get("fromUserInfo");
    }

    public GroupMessage setFromUserInfo(Long fromUserInfo) {
        this.set("fromUserInfo", fromUserInfo);
        return this;
    }

    public Long getTo() {
        return (Long) this.get("to");
    }

    public GroupMessage setTo(Long to) {
        this.set("to", to);
        return this;
    }

    public String getType() {
        return (String) this.get("type");
    }

    public GroupMessage setType(String type) {
        this.set("type", type);
        return this;
    }

    public String getBody() {
        return (String) this.get("body");
    }

    public GroupMessage setBody(String body) {
        this.set("body", body);
        return this;
    }

    public String getExtra() {
        return (String) this.get("extra");
    }

    public GroupMessage setExtra(String extra) {
        this.set("extra", extra);
        return this;
    }

    public Integer getIsRead() {
        return (Integer) this.get("isRead");
    }

    public GroupMessage setIsRead(Integer isRead) {
        this.set("isRead", isRead);
        return this;
    }

    public Integer getIsRevoke() {
        return (Integer) this.get("isRevoke");
    }

    public GroupMessage setIsRevoke(Integer isRevoke) {
        this.set("isRevoke", isRevoke);
        return this;
    }

    public Integer getIsDeleted() {
        return (Integer) this.get("isDeleted");
    }

    public GroupMessage setIsDeleted(Integer isDeleted) {
        this.set("isDeleted", isDeleted);
        return this;
    }

    public String getStatus() {
        return (String) this.get("status");
    }

    public GroupMessage setStatus(String status) {
        this.set("status", status);
        return this;
    }

    public Integer getReceive() {
        return (Integer) this.get("receive");
    }

    public GroupMessage setReceive(Integer receive) {
        this.set("receive", receive);
        return this;
    }

    public Long getTime() {
        return (Long) this.get("time");
    }

    public GroupMessage setTime(Long time) {
        this.set("time", time);
        return this;
    }
}
