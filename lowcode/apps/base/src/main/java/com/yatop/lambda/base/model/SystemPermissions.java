package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

@Table(name = "base_system_permissions")
public class SystemPermissions extends Model<SystemPermissions> {
    @Id
    private Long id;


    @Column(label = "权限编码")
    private String auth;


    @Column(label = "权限名称")
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;




    @Service(displayName = "保存系统权限")
    public void saveSystemPermissions(String model, String service){
        System.out.println(1);
    }


    public Long getRole(){
        return this.getLong("role");
    }


    public Long getId() {
        return (Long) this.get("id");
    }

    public SystemPermissions setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getAuth() {
        return (String) this.get("auth");
    }

    public SystemPermissions setAuth(String auth) {
        this.set("auth", auth);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public SystemPermissions setName(String name) {
        this.set("name", name);
        return this;
    }
}
