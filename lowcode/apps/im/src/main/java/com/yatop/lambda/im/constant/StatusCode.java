package com.yatop.lambda.im.constant;

public enum StatusCode {

    //签名错误
    SIGN_ERROR(10000, "签名校验错误"),

    //网络连接错误
    CONNECT_ERROR(10001, "聊天服务网络连接错误"),
    //用户验权
    TOKEN_ERROR(10002, "用户验证失败，请检查userId、token"),

    //用户未登录
    LOGIN_EXPIRE(10003, "用户登录状态过期，请重新登录"),

    //会话不存在
    CONVERSATION_NOT_FOUND(10004, "会话不存在"),

    //接收者ID错误
    TOUSER_ID_NOT_FOUND(10005, "接收者ID错误"),

    //ID错误
    ID_ERROR(10006, "ID错误"),

    //您已被当前用户拉黑，无法向他发送消息
    MESSAGE_REJECTED(10007, "您已被当前用户拉黑，无法向他发送消息"),

    //参数错误
    PARAMS_ERROR(10008, "参数错误"),

    //多端挤下线
    KICKED_OUT(10009, "有新连接踢掉了当前连接"),

    //上传错误
    UPLOAD_ERROR(10010, "上传错误"),

    //群组全体禁言
    GROUP_ALL_MUTE(10011, "当前群组全体禁言"),

    //被禁言
    GROUP_MUTE(10012, "您在当前群组内已被禁言"),

    //非群成员，无法执行此操作
    NO_GROUP_USER(10013, "非群成员，无法执行此操作"),

    //群ID重复
    GROUP_DUPLICATE(10015, "群ID重复"),

    //无权限操作群相关修改
    GROUP_PERMISSION_DENIED(10016, "无权限操作"),

    //用户不存在
    USER_NOT_FOUND(10017, "用户不存在"),

    //群主只能解散，群主不能退出
    GROUP_ONLY_DISSOLVE(10018, "群主只能解散，不能退出"),

    //用户重复
    USER_DUPLICATE(10019, "用户重复"),

    //过期时间设置错误
    EXPIRE_ERROR(10020, "过期时间设置错误"),

    //对方拒绝添加好友
    USER_FRIEND_DENY_FOUND(10021, "对方拒绝添加好友"),

    //好友重复
    USER_FRIEND_DUPLICATE(10022, "好友重复"),

    //频率限制
    FREQUENCY_LIMIT(10023, "频率限制"),


    //未找到此好友
    FRIEND_NOT_FOUND(10025, "未找到此好友"),

    //未找到此好友申请
    FRIEND_APPLY_NOT_FOUND(10026, "未找到此好友申请"),

    //重复操作
    DUPLICATE_ERROR(10027, "重复操作"),

    //好友申请：已发送申请，请等待对方处理
    APPLY_NEED(20020, "已发送申请，请等待对方处理"),

    //当前群组不存在或已解散
    GROUP_NOT_FOUND(10201, "当前群组不存在或已解散"),

    //当前群组设置禁止加群
    GROUP_NO_ENTRY(10202, "当前群组设置禁止加群"),

    //群用户单次添加数量超过限制
    GROUP_USER_INSERT_LIMIT(10203, "群用户单次添加数量不能超过500"),

    //登陆成功
    LOGIN_SUCCESS(201, "登陆成功"),

    //心跳
    HEART(202, ""),

    //会话更新
    CONVERSATION_CHANGED(203, "会话更新"),

    //会话列表更新
    CONVERSATION_LIST_CHANGED(204, "会话列表更新"),

    //私聊会话已读回执
    PRIVATE_READ_RECEIPT(205, "会话已读回执"),

    //入群申请事件
    GROUP_APPLY_RECEIVE(206, "入群申请"),

    //消息撤回事件
    MESSAGE_REVOKED(207, "消息撤回"),

    //好友列表更新事件
    FRIEND_LIST_CHANGED(208, "好友列表更新"),

    //好友申请列表更新事件
    FRIEND_APPLY_LIST_CHANGED(209, "好友申请列表更新"),

    //好友申请被拒绝
    FRIEND_APPLY_REFUSE(210, "好友申请被拒绝"),

    //推送新消息
    MESSAGE_RECEIVE(200, "");

    private Integer code;
    private String desc;

    private StatusCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private StatusCode() {
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}