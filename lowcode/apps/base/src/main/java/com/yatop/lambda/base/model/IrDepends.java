package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_depends", displayName = "应用依赖")
public class IrDepends extends Model<IrDepends> {
    @Id
    private Long id;


    @Column(name = "name",label = "应用名称")
    private String name;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private IrModule baseApp;

    public Long getId() {
        return (Long) this.get("id");
    }

    public IrDepends setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public IrDepends setName(String name) {
        this.set("name", name);
        return this;
    }

//    public IrModule getBaseApp() {
//        return (IrModule) this.get("baseApp");
//    }

    public Long getBaseApp(){
        return this.getLong("baseApp");
    }

    public IrDepends setBaseApp(IrModule baseApp) {
        this.set("baseApp", baseApp);
        return this;
    }
}
