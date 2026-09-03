package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Date;
@Table(displayName = "好友申请", name = "im_friend_apply")
public class FriendApply  extends Model<FriendApply> {

    @Id
    private Long id;

    @Column(label = "申请人")
    private Long applyUserId;

    @Column(label = "用户id")
    private String userId;

    @Column(label = "备注")
    private String remark;

    @Column(label = "扩展信息")
    private String extraMessage;

    @Column(label = "状态")
    private Integer status;

    @Column(label = "转交时间")
    private Long transformTime;

    @Column(label = "是否已读")
    private Integer isRead;

    @Column(label = "创建时间")
    private Date createdAt;
}
