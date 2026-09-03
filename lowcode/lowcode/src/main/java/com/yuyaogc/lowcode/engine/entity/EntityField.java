package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 模型字段
 */
public class EntityField extends Entity implements Serializable {
    private EntityClass entity;
    private boolean pk;
    private String columnName;
    private DataType dataType;

    private Integer length;

    private boolean store;
    private boolean inherited;
    private Map<String, Validate> validates = new HashMap<>();

    private String relModel;
    private String relModel2;

    private String relApp;
    private String referencedProperty;

    private String inverseName;

    private String joinColumnName;




    public String getRelModel2() {
        return relModel2;
    }

    public void setRelModel2(String relModel2) {
        this.relModel2 = relModel2;
    }

    public String getJoinColumnName() {
        return joinColumnName;
    }

    public void setJoinColumnName(String joinColumnName) {
        this.joinColumnName = joinColumnName;
    }

    public String getInverseName() {
        return inverseName;
    }

    public void setInverseName(String inverseName) {
        this.inverseName = inverseName;
    }

    public EntityField() {
    }

    public EntityField(EntityClass entity) {
        this.entity = entity;
    }

    public EntityClass getEntity() {
        return entity;
    }

    public void setEntity(EntityClass entity) {
        this.entity = entity;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public String getReferencedProperty() {
        return referencedProperty;
    }

    public void setReferencedProperty(String referencedProperty) {
        this.referencedProperty = referencedProperty;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getLength() {
        if(null == length || length == 255){
            return dataType.getSize(this);
        }
        return length;
    }

    public Integer getScale(){
        return dataType.getScale(this);
    }

    public Map<String, Validate> getValidates() {
        return validates;
    }

    public String getRelApp() {
        return relApp;
    }

    public void setRelApp(String relApp) {
        this.relApp = relApp;
    }


    public String getRelModel() {
        return relModel;
    }

    public void setRelModel(String relModel) {
        this.relModel = relModel;
    }

    public void setValidates(Map<String, Validate> validates) {
        this.validates = validates;
    }

    public void addValidate(Validate validate) {
        validates.put(this.getName(), validate);
    }


    public void removeValidate() {
        validates.remove(this.getName());
    }

    protected List<EntityField> resolveMro(EntityClass model, String name) {
        List<EntityField> result = new ArrayList<>();
        for (EntityClass cls : model.getMro()) {
            if (cls.getApplication() == null) {
                EntityField field = cls.getField(name);
                if (field != null) {
                    result.add(field);
                }
            }
        }
        return result;
    }


    @Override
    public String toString() {
        return "EntityField{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
