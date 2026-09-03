package com.yatop.lambda.base.model;


import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "base_entity",displayName = "模型")
public class IrModel extends Model<IrModel> {
    @Id
    private Long id;
    @Column(name = "app_name",label = "应用名")
    @NotBlank
    private String appName;
    @Column(name = "entity_name",label = "模型名")
    @NotBlank
    private String entityName;
    @Column(name = "table_name",label = "表名")
    private String tableName;
    @NotBlank
    @Column(name = "display_name", label = "中文名")
    private String displayName;
    @Column(name = "parent", label = "扩展继承")
    private String parent;

    @OneToMany
    private List<IrField> fieldList;


    @OneToMany
    private List<IrMethod> methodList;

    @Service(displayName = "创建")
    public void create(IrModel baseEntity) {
        Container container = Container.me();

        Application application = container.get(baseEntity.getStr("appName"));
        if (Objects.isNull(application)) {
            return;
        }

        EntityClass entityClass = new EntityClass();
        entityClass.setApplication(application);
        entityClass.setName(baseEntity.getStr("tableName"));
        entityClass.setDisplayName(baseEntity.getStr("displayName"));
        entityClass.setTableName(baseEntity.getStr("tableName"));

        String parent = baseEntity.getStr("parent");
        if (StringUtils.isNotEmpty(parent)) {
            entityClass.setParent(parent.split(","));
        }

        try {
            Class clazz = Class.forName("com.yuyaogc.lowcode.engine.plugin.activerecord.Model");
            ClassUtils.buildMethod(entityClass, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        EntityField entityField = new EntityField();
        entityField.setName("id");
        entityField.setEntity(entityClass);
        entityField.setColumnName("id");
        entityField.setPk(true);
        entityField.setStore(true);
        entityField.setDataType(DataType.create(Constants.LONG));
        entityClass.addPkField("id", entityField);
        entityClass.addField("id", entityField);

        application.buildModel(baseEntity.getStr("entityName"), entityClass);

        for (EntityClass entityClass1 : application.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, container);
        }

        try (Context context = new Context(null, Db.getConfig())) {
            application.autoTableInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        baseEntity.save();
    }

    public Long getId() {
        return (Long) this.get("id");
    }

    public IrModel setId(Long id) {
        this.set("id", id);
        return this;
    }

    public String getAppName() {
        return (String) this.get("appName");
    }

    public IrModel setAppName(String appName) {
        this.set("appName", appName);
        return this;
    }

    public String getEntityName() {
        return (String) this.get("entityName");
    }

    public IrModel setEntityName(String entityName) {
        this.set("entityName", entityName);
        return this;
    }

    public String getTableName() {
        return (String) this.get("tableName");
    }

    public IrModel setTableName(String tableName) {
        this.set("tableName", tableName);
        return this;
    }

    public String getDisplayName() {
        return (String) this.get("displayName");
    }

    public IrModel setDisplayName(String displayName) {
        this.set("displayName", displayName);
        return this;
    }

    public String getParent() {
        return (String) this.get("parent");
    }

    public IrModel setParent(String parent) {
        this.set("parent", parent);
        return this;
    }
}
