package com.yatop.lambda.demo;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
@Table(name = "demo_many_many", displayName = "多对多")
public class DemoMany2many extends Model<DemoMany2many> {

    @Id
    private Long id;
    @Column(label = "名称")
    private String name;
}
