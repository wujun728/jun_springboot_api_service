package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "base_role",displayName = "角色")
public class Role extends Model<Role> {
    @Id
    private Long id;
    @Column(label = "角色编码")
    private String roleKey;
    @Column(label = "角色名称")
    private String roleName;
    @Column(label = "是否管理员")
    private Boolean admin;
    @ManyToMany
    @JoinTable(name = "base_role_user",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;
    @ManyToMany
    @JoinTable(name = "base_role_permission",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permissions> permissionsList;

    public List<IrUiMenu> getChildren(String key, List<IrUiMenu> menus) {
        return menus.stream()
                .filter(irUiMenu -> StringUtils.equals(key, irUiMenu.getParentId()))
                .collect(Collectors.toList());
    }
    public void children2(List<IrUiMenu> menus, IrUiMenu uiMenu, List<IrUiMenu> childList) {
        List<IrUiMenu> children = getChildren(uiMenu.getKey(), menus);



        for (IrUiMenu irUiMenu : children) {

            List<IrUiMenu> subMenuList = new ArrayList<>();
            irUiMenu.put("label",irUiMenu.getName());
            irUiMenu.put("value", String.format("%s", irUiMenu.getKey()));

            children2(menus, irUiMenu, subMenuList);

            irUiMenu.put("children", subMenuList);

            if(subMenuList.isEmpty()){
                EntityClass entityClass = Container.me().getEntityClass(irUiMenu.getModel());
                if(null != entityClass){
                    List<Map<String,Object>> methodItems = new ArrayList<>();
                    for(LinkedList<EntityMethod> methods: entityClass.getMethods()){
                        for(EntityMethod method: methods){

                            Map<String,Object> methodItem = new HashMap<>();
                            methodItem.put("label",method.getDisplayName());
                            methodItem.put("value", String.format("%s.%s.%s", entityClass.getApplication().getName(), entityClass.getName(), method.getName()));

                            methodItems.add(methodItem);
                        }
                    }
                    irUiMenu.put("children", methodItems);
                }
            }

            childList.add(irUiMenu);
        }
    }

    @Service(displayName = "获取服务权限")
    public List<IrUiMenu> listPermissions(Long roleId){

        LambdaQueryWrapper<SystemPermissions> lambdaQueryWrapper = Wrappers.lambdaQuery();
        List<SystemPermissions> systemPermissionsList = new SystemPermissions().search(lambdaQueryWrapper.eq(SystemPermissions::getRole, roleId), 0,0,null);


        List<IrUiMenu> menus =  new IrUiMenu().search(new Criteria(), 0,0,null);


        List<IrUiMenu> permissions = new ArrayList<>();
        List<IrUiMenu> parents = getChildren(null, menus);
        for(IrUiMenu uiMenu: parents){
            uiMenu.put("label", uiMenu.getName());
            uiMenu.put("value", uiMenu.getKey());

            List<IrUiMenu> childList = new ArrayList<>();
            children2(menus, uiMenu, childList);

            uiMenu.put("children",childList);
            permissions.add(uiMenu);
        }



        return permissions;
    }



    public Role setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getRoleKey() {
        return (String) this.get("roleKey");
    }

    public Role setRoleKey(String roleKey) {
        this.set("roleKey", roleKey);
        return this;
    }

    public String getRoleName() {
        return (String) this.get("roleName");
    }

    public Role setRoleName(String roleName) {
        this.set("roleName", roleName);
        return this;
    }

    public Long getId(){
        return getLong("id");
    }

    public boolean isAdmin(){
        return getBoolean("admin");
    }
}
