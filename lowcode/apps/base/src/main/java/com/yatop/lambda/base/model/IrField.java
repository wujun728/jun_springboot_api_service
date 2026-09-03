package com.yatop.lambda.base.model;

import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;

import java.util.Objects;

@Table(name = "base_field", displayName = "字段")
public class IrField extends Model<IrField> {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "column_name")
    @NotBlank
    private String columnName;
    @Column(name = "data_type")
    private String dataType;
    @Column(name = "entity_name")
    private String entityName;
    @Column(name = "app_name")
    private String appName;
    @ManyToOne
    @JoinColumn(name = "model_id")
    private IrModel irModel;

    @Service
    public void create(IrField baseField) {
        Container container = Container.me();

        Application application = container.get(baseField.getStr("appName"));
        if (Objects.isNull(application)) {
            return;
        }

        EntityClass entityClass = application.getEntity(baseField.getStr("entityName"));

        EntityField entityField = new EntityField();
        entityField.setName(baseField.getStr("name"));
        entityField.setEntity(entityClass);
        entityField.setColumnName(baseField.getStr("columnName"));
        entityField.setStore(true);
        entityField.setDataType(DataType.create(baseField.getStr("dataType")));
        entityClass.addField(baseField.getStr("name"), entityField);

        application.buildModel(baseField.getStr("entityName"), entityClass);

        for (EntityClass entityClass1 : application.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, container);
        }

        try (Context context = new Context(null, Db.getConfig())) {
            application.autoTableInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        baseField.save();
    }
}
