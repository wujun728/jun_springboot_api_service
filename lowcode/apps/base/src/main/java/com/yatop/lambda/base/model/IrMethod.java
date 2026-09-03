package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_method", displayName = "服务")
public class IrMethod extends Model<IrMethod> {


    @Id
    private Long id;

    @Column(label = "类名")
    private String className;


    @ManyToOne
    @JoinColumn(name = "model_id")
    private IrModel irModel;


}
