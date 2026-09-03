package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;


@Table(displayName = "群组申请", name = "im_group_apply")
public class GroupApply extends Model<GroupApply> {
    @Id
    private Long id;

    /**
     * 群ID
     */
    @Column(label = "群ID")
    private Long groupId;


    /**
     * 申请用户ID
     */
    @Column(label = "申请用户ID")
    private Long userId;



    /**
     * 邀请人id
     * 如果是别人代拉userid入群，此字段为操作者ID
     */
    @Column(label = "邀请人id")
    private String inviterId;

    /**
     * 申请附言
     */
    @Column(label = "申请附言")
    private String extraMessage;

    /**
     * 申请状态
     */
    @Column(label = "申请状态")
    private Integer status;

    /**
     * 处理申请的管理员ID
     */
    @Column(label = "处理申请的管理员ID")
    private String adminId;

    /**
     * 处理时间
     */
    @Column(label = "处理时间")
    private Long transformTime;

    /**
     * 处理附言
     */
    @Column(label = "处理附言")
    private String transformMessage;

    /**
     * 申请的时间
     */
    @Column(label = "申请的时间")
    private Long createdAt;
}
