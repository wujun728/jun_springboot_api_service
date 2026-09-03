package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_post", displayName = "朋友圈")
public class Post extends Model<Post> {

    @Column(name = "id")
    private Long id;

    @Column(name = "post_name", label = "文章标题")
    private String postName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "fabulous_num", label = "点赞数")
    private Integer fabulousNum;

    @Column(name = "role")
    private Integer role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "urls", label = "文章图片")
    private String urls;

    @Column(name = "post_context", label = "文章内容")
    private String postContext;
}
