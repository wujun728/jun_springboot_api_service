package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_comment", displayName = "im评论")
public class Comment extends Model<Comment> {
    @Column(name = "id")
    private Integer id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "reply_user_id")
    private String replyUserId;

    @Column(name = "reply_user_name")
    private String replyUserName;

    @Column(name = "post_comment")
    private String postComment;

}
