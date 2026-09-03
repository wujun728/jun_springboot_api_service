package com.yatop.lambda.demo;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.OneToMany;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.List;

@Table(name = "demo_many_one", displayName = "多对一")
public class DemoMan2one extends Model<DemoMan2one> {

    @Id
    private Long id;

    @Column(label = "名称")
    private String name;


    @OneToMany
    private List<DemoOne2Many> demoOne2ManyList;
}
