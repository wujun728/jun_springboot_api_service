package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.Column;
import com.yuyaogc.lowcode.engine.annotation.Id;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.exception.EngineErrorEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.IdWorker;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Table(name = "base_permissions",displayName = "权限")
public class Permissions extends Model<Permissions> {





    @Id
    private Long id;

    @Column(label = "名称")
    private String name;

    @Column(label = "权限码")
    private String auth;





    @Service(displayName = "检查权限")
    public boolean checkPermissions(Long userId, String auth){
       List<Role> roleList = new Role().search(Criteria.equal("userList", userId),0,0,null);
       if(roleList.isEmpty()){
           throw new EngineException(EngineErrorEnum.Authenticator);
       }
       boolean isAdmin =  roleList.stream().anyMatch(r->r.isAdmin());
       if(!isAdmin){
           LambdaQueryWrapper<Permissions> wrapper = Wrappers.lambdaQuery();
        Permissions permissions =   new Permissions().selectOne(wrapper.eq(Permissions::getAuth, auth));

        if(roleList.isEmpty()){
            throw new EngineException(EngineErrorEnum.Authenticator);
        }

        List<Long> permissionList = (List<Long>) roleList.get(0).get("permissionsList");
        if(permissions==null || !permissionList.contains(permissions.getId())){
            throw new EngineException(EngineErrorEnum.Authenticator);
        }

        //TODO 通配符

       }
        return true;
    }










    public Long getId() {
        return (Long) this.get("id");
    }

    public Permissions setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public Permissions setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getAuth() {
        return (String) this.get("auth");
    }

    public Permissions setAuth(String auth) {
        this.set("auth", auth);
        return this;
    }
}
