package com.yatop.lambda.demo;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "demo_one_many", displayName = "一对多")
public class DemoOne2Many extends Model<DemoMan2one> {
    @Id
    private Long id;
    @Column(label = "a")
    private String a;
}
