package com.yatop.lambda.base.model;

import com.yatop.lambda.base.model.views.components.Page;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.loader.Loader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import com.yuyaogc.lowcode.engine.wrapper.LambdaQueryWrapper;
import com.yuyaogc.lowcode.engine.wrapper.Wrappers;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@APP(name = "base", displayName = "基础应用")
@Table(name = "base_module")
public class IrModule extends Model<IrModule> {

    @Id
    private Long id;

    @Column(name = "app_name",label = "应用名")
    @NotBlank
    private String appName;
    @Column(name = "display_name",label = "中文名")
    private String displayName;

    @Column(name = "version",label = "版本")
    private String version;

    // 0 installed  1 uninstall 2 installing 3
    @Column(name = "state", label = "状态")
    private int state;

    @Column(name = "img_url",label = "封面")
    private String imgUrl;

    @Column(name = "type",label = "类型")
    private String type;

    @Column(name = "jar_url",label = "程序包")
    private String jarUrl;


    @OneToMany
    private List<IrDepends> dependsList;


    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;


    @Service(displayName = "创建")
    public void create(IrModule value) throws IOException {
        List<Map<String,Object>> files = (List<Map<String, Object>>) value.get("file");
        String name = (String) files.get(0).get("name");

        IrModule  module =   selectOne(Criteria.equal("jarUrl", name));

        Loader loader = Loader.getLoader();
       APP app = loader.getAppInfo(name, "com.yatop.lambda");


        if(module == null){
            value.remove("file");
            value.setJarUrl(name);
            value.setDisplayName(app.name());
            value.setAppName(app.name());
            value.setVersion("master");
            value.setState(0);
            value.setType(app.type().name());
            value.save();

            for(String depend: app.depends()){
                IrDepends depends = new IrDepends();
                depends.set("baseApp", value.getLong("id"));
                depends.setName(depend);
                depends.save();
            }
        } else {
            value.remove("file");
            value.remove("dependsList");

            LambdaQueryWrapper<IrDepends> wrapper = Wrappers.lambdaQuery();
            List<IrDepends> dependList =  new IrDepends().search(wrapper.eq(IrDepends::getBaseApp,module.getLong("id")), 0,0,null);
            List<Long> dependIds =  dependList.stream().map(IrDepends::getId).collect(Collectors.toList());
            if(!dependIds.isEmpty()){
                new IrDepends().deleteByIds(dependIds.toArray());
            }


            for(String depend: app.depends()){
                IrDepends depends = new IrDepends();
                depends.set("baseApp", module.get("id"));
                depends.setName(depend);
                depends.save();
            }


            value.set("id", module.get("id"));
            value.setJarUrl(name);
            value.setState(0);
            value.setDisplayName(app.displayName());
            value.setAppName(app.name());
            value.setVersion("master");
            value.setType(app.type().name());
            value.update();

            //TODO 修改依赖app
        }
    }


    @Service(displayName = "安装")
    public void install(String ids) throws IOException {
        System.out.println(1);

        Loader loader =   Loader.getLoader();

        if(StringUtil.isNotEmpty(ids)) {

            List<Model> modules =   this.search(Criteria.in("id", (Object) Arrays.asList(ids.split(","))),0,0,null);
            List<Long> moduleIds = modules.stream().map(model -> model.getLong("id")).collect(Collectors.toList());
            List<Model> depends =  getContext().get("base.base_depends").search(Criteria.in("baseApp", (Object) moduleIds), 0, 0, null);

            List<String> jarUrlList =  loader.getALLJarList(modules, depends);

            loader.doInstalls(jarUrlList);

        }
    }

    public String getVersion() {
        return version;
    }

    public IrModule setVersion(String version) {
        this.set("version", version);
        return this;
    }

    public String getDisplayName() {
        return (String) this.get("displayName");
    }

    public IrModule setDisplayName(String displayName) {
        this.set("displayName", displayName);
        return this;
    }

    public String getAppName() {
        return (String) this.get("appName");
    }

    public IrModule setAppName(String appName) {
        this.set("appName", appName);
        return this;
    }

    public int getState() {
        return (int) this.get("state");
    }

    public IrModule setState(int state) {
        this.set("state", state);
        return this;
    }

    public String getImgUrl() {
        return (String) this.get("imgUrl");
    }

    public IrModule setImgUrl(String imgUrl) {
        this.set("imgUrl", imgUrl);
        return this;
    }

    public String getType() {
        return (String) this.get("type");
    }

    public IrModule setType(String type) {
        this.set("type", type);
        return this;
    }

    public String getJarUrl() {
        return (String) this.get("jarUrl");
    }

    public IrModule setJarUrl(String jarUrl) {
        this.set("jarUrl", jarUrl);
        return this;
    }

    public String getUpdateUser() {
        return (String) this.get("updateUser");
    }

    public IrModule setUpdateUser(String updateUser) {
        this.set("updateUser", updateUser);
        return this;
    }
}
