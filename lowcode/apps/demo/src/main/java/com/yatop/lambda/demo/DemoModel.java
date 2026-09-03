package com.yatop.lambda.demo;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Date;
import java.util.List;

@APP(name = "demo", displayName = "模板")
@Table(name = "demo_model",displayName = "模板")
public class DemoModel extends Model<DemoModel> {
    @Id
    private Long id;

    @Column(label = "字符串")
    private String a;

    @Column(label = "长整型")
    private Long b;

    @Column(label = "整型")
    private Integer c;

    @Column(label = "双精度")
    private Double d;

    @Column(label = "浮点型")
    private Float e;

    @Column(label = "日期")
    private Date f;

    @File(label = "文件", name = "g")
    private String g;

    @Column(label = "逻辑")
    private Boolean p;


    @Selection(
          options =   {
                  @Option(value = "base", label= "基础"),
                  @Option(value = "custom", label = "自定义")
          }
    )
    @Column(label = "下拉")
    private String l;



    @ManyToOne
    @JoinColumn(name = "demo_many_one")
    private DemoMan2one demoMan2one;


    @ManyToMany
    @JoinTable(name = "demo_many_many_many",
            joinColumns = @JoinColumn(name = "d_id"),
            inverseJoinColumns = @JoinColumn(name = "m_id"))
    private List<DemoMany2many> demoMany2manyList;
}

