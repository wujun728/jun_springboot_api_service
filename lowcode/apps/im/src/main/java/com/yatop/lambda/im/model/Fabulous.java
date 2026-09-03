package com.yatop.lambda.im.model;

import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "im_fabulous", displayName = "点赞数")
public class Fabulous extends Model<Fabulous> {
    @Column(name = "id")
    private Integer id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "fabulous_num")
    private Integer fabulousNum;

}
