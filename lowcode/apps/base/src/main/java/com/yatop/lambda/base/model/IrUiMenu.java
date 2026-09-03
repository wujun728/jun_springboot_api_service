package com.yatop.lambda.base.model;

import cn.hutool.core.lang.tree.Tree;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.plugin.activerecord.KvMap;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Table(name = "base_ui_menu")
public class IrUiMenu extends Model<IrUiMenu> {
    @Id
    private Long id;
    @Column(label = "菜单名称")
    private String name;
    @Column(label = "菜单路径")
    private String url;
    @Column(label = "模型")
    private String model;

    @Column(label = "模组")
    private String module;

    @Column(label ="单击")
    private String click;

    @Column(label = "样式")
    private String css;

    @Column(label = "视图")
    private String view;

    @Column(label = "序号")
    private Integer sequence;

    /**
     * 菜单状态（0正常 1停用）
     */
    @Column(label = "状态")
    private String status;

    /**
     * 权限字符串
     */
    @Column(label = "权限")
    private String perms;

    /**
     * 菜单图标
     */
    @Column(label = "图标")
    private String icon;
    /**
     * 备注
     */
    @Column(label = "备注")
    private String remark;

    @Column(label = "唯一")
    private String key;


    public String getPerms(){
        return getStr("perms");
    }

    //@OneToMany
    //private List<Menu> children;


    @Column(name = "parent_id",label = "父")
    private  String parentId;


    public String getParentId(){
        return getStr("parentId");
    }

    public Long getId() {
        return (Long) this.get("id");
    }

    public IrUiMenu setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getName() {
        return (String) this.get("name");
    }

    public IrUiMenu setName(String name) {
        this.set("name", name);
        return this;
    }

    public String getUrl() {
        return (String) this.get("url");
    }

    public IrUiMenu setUrl(String url) {
        this.set("url", url);
        return this;
    }

    public String getModel() {
        return (String) this.get("model");
    }

    public IrUiMenu setModel(String model) {
        this.set("model", model);
        return this;
    }

    public String getClick() {
        return (String) this.get("click");
    }

    public IrUiMenu setClick(String click) {
        this.set("click", click);
        return this;
    }

    public String getCss() {
        return (String) this.get("css");
    }

    public IrUiMenu setCss(String css) {
        this.set("css", css);
        return this;
    }

    public String getView() {
        return (String) this.get("view");
    }

    public IrUiMenu setView(String view) {
        this.set("view", view);
        return this;
    }

    public Integer getSequence() {
        return (Integer) this.get("sequence");
    }

    public IrUiMenu setSequence(Integer sequence) {
        this.set("sequence", sequence);
        return this;
    }

    public String getStatus() {
        return (String) this.get("status");
    }

    public IrUiMenu setStatus(String status) {
        this.set("status", status);
        return this;
    }

    public IrUiMenu setPerms(String perms) {
        this.set("perms", perms);
        return this;
    }

    public String getIcon() {
        return (String) this.get("icon");
    }

    public IrUiMenu setIcon(String icon) {
        this.set("icon", icon);
        return this;
    }

    public String getRemark() {
        return (String) this.get("remark");
    }

    public IrUiMenu setRemark(String remark) {
        this.set("remark", remark);
        return this;
    }

    public String getKey() {
        return (String) this.get("key");
    }

    public IrUiMenu setKey(String key) {
        this.set("key", key);
        return this;
    }

    public IrUiMenu setParentId(String parentId) {
        this.set("parentId", parentId);
        return this;
    }

    @Service
    public List<Tree<Long>> treeselect(){
        return null;
    }


    @Service(displayName = "加载菜单")
    public Map<String,Object> loadMenus(){
        List<IrUiMenu> menus =  this.search(new Criteria(),0,0, null);

        List<KvMap> menuList = new ArrayList<>();
        KvMap result = new KvMap();
        result.put("pages", menuList);


        KvMap index = new KvMap();
        index.put("label", "Home");
        index.put("url", "/");
        index.put("redirect", "/index/1");
        menuList.add(index);


        List<IrUiMenu> parents = getChildren(null, menus);
        for(IrUiMenu uiMenu: parents){
            KvMap menu = new KvMap();
            menu.put("label",uiMenu.getName());
            menu.put("schemaApi", "/api/rpc/views?key=" + uiMenu.getView()  + "&model=" + uiMenu.getModel() +"&module=" + uiMenu.getModule());
            List<KvMap> menu1List = new ArrayList<>();
            children(menus, uiMenu, menu1List);
            menu.put("children",menu1List);
            menuList.add(menu);
        }
        //TODO
        return result;
    }


    @Service(displayName = "搜索")
    public List<IrUiMenu> search2(Criteria criteria, Integer offset, Integer limit, String order) {
        List<IrUiMenu> menus =  this.search(new Criteria(),0,0, null);
        List<IrUiMenu> menuList = new ArrayList<>();
        List<IrUiMenu> parents = getChildren(null, menus);
        for(IrUiMenu uiMenu: parents){
            List<IrUiMenu> menu1List = new ArrayList<>();
            children2(menus, uiMenu, menu1List);
            uiMenu.put("children",menu1List);
            menuList.add(uiMenu);
        }
        return menuList;
    }


    public void children2(List<IrUiMenu> menus, IrUiMenu uiMenu, List<IrUiMenu> menu1List) {
        List<IrUiMenu> children = getChildren(uiMenu.getKey(), menus);
        for (IrUiMenu irUiMenu : children) {
            List<IrUiMenu> subMenuList = new ArrayList<>();
            children2(menus, irUiMenu, subMenuList);
            irUiMenu.put("children", subMenuList);
            menu1List.add(irUiMenu);
        }
    }
    public void children(List<IrUiMenu> menus, IrUiMenu uiMenu, List<KvMap> menu1List) {
        List<IrUiMenu> children = getChildren(uiMenu.getKey(), menus);
        for (IrUiMenu irUiMenu : children) {
            KvMap menu = new KvMap();
            menu.put("label", irUiMenu.getName());
            menu.put("schemaApi", "/api/rpc/views?key=" + irUiMenu.getView()  + "&model=" + irUiMenu.getModel() +"&module=" + irUiMenu.getModule());
            List<KvMap> subMenuList = new ArrayList<>();
            children(menus, irUiMenu, subMenuList);
            menu.put("children", subMenuList);
            menu1List.add(menu);
        }
    }



    public List<IrUiMenu> getChildren(String key, List<IrUiMenu> menus) {
        return menus.stream()
                .filter(irUiMenu -> StringUtils.equals(key, irUiMenu.getParentId()))
                .collect(Collectors.toList());
    }

    public String getModule() {
        return (String) this.get("module");
    }

    public IrUiMenu setModule(String module) {
        this.set("module", module);
        return this;
    }
}
