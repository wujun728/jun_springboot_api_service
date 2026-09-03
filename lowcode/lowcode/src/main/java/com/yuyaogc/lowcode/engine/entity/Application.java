package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.cglib.CglibInvocation;
import com.yuyaogc.lowcode.engine.cglib.Invocation;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.enums.AppStateEnum;
import com.yuyaogc.lowcode.engine.enums.AppTypeEnum;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Column;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 应用
 */
public class Application extends Entity{
    private static Logger log = LoggerFactory.getLogger(Application.class);
    private Map<String, EntityClass> entityClasss = new HashMap<>();

    private List<Class<?>> classList = new ArrayList<>();

    private AppClassLoader appClassLoader;

    private String[] dependencies;
    private String version;
    private String category;
    private double weight = 1.0;
    private AppStateEnum stateEnum;

    private AppTypeEnum typeEnum;

    private String fileName;
    private List<Application> depends;


    public AppClassLoader getAppClassLoader() {
        return appClassLoader;
    }

    public void setAppClassLoader(AppClassLoader appClassLoader) {
        this.appClassLoader = appClassLoader;
    }

    public AppTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(AppTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public List<Application> getDepends() {
        return depends;
    }

    public void setDepends(List<Application> depends) {
        this.depends = depends;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isBaseApp() {
        return false;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
    public boolean containsKey(String className){
        return entityClasss.containsKey(className);
    }

    public Collection<EntityClass> getModels() {
        return entityClasss.values();
    }

    public EntityClass getEntity(String name) {
        return entityClasss.get(name);
    }

    public void buildModel(String name, EntityClass entity) {
        entityClasss.put(name, entity);
    }

    public void remove(String name){
        entityClasss.remove(name);
    }

    public List<Class<?>> getClassList() {
        return classList;
    }

    public void setClassList(List<Class<?>> classList) {
        this.classList = classList;
    }

    public void setAppInfo(APP appInfo){
        this.setName(appInfo.name());
        this.setDisplayName(appInfo.displayName());
        this.setDependencies(appInfo.depends());
        this.setVersion(appInfo.version());
        this.setTypeEnum(appInfo.type());
    }


    public void onDestroy(){
        Invocation invocation = new CglibInvocation();
        for (EntityClass entity : getModels()) {
            for(EntityMethod entityMethod: entity.getDestroys()){
                try {
                    invocation.invoke(entityMethod.getMethod());
                } catch (Exception e) {
                    log.error("销毁事件 {}",e);
                }
            }
        }
    }


    public void onEvent(){
        Invocation invocation = new CglibInvocation();
        for (EntityClass entity : getModels()) {
            for(EntityMethod entityMethod: entity.getEvents()){
                try {
                    invocation.invoke(entityMethod.getMethod());
                } catch (Exception e) {
                    log.error("启动事件 {}",e);
                }
            }
        }
    }







    public void buildClass(){
        for(Class clazz: this.getClassList()){
            ClassUtils.buildClass(clazz, this);
        }
    }


    public void buildInherit(){
        for (EntityClass entityClass1 : this.getModels()) {
            ClassBuilder.buildEntityClass(entityClass1, Container.me());
        }
    }


    public void loadSeedData(){
        ClassUtils.loadSeedData(this.getName());
    }


    public void autoTableInit() {
        Config config = Context.getInstance().getConfig();
        for (EntityClass entity : getModels()) {
            boolean tableExists = config.dialect.tableExists(config, entity.getTableName());
            if (!tableExists) {
                config.dialect.createModelTable(config, entity.getTableName(), entity.getDisplayName());
            }
            Map<String, Column> columns = config.dialect.tableColumns(config, entity.getTableName());
            entity.getFields().forEach(field -> {

                Column column = columns.get(field.getColumnName());
                if (Objects.isNull(column)) {
                    if (field.getDataType() instanceof DataType.One2manyField || field.getDataType() instanceof DataType.Many2manyField) {
                    } else {
                        config.dialect.createColumn(config, entity.getTableName(), field.getColumnName(), config.dialect.getColumnType(field.getDataType().getType(), field.getDataType().getSize(field), null), field.getDisplayName(), false);
                    }
                } else {

                    if (!column.getLength().equals(field.getLength()) ||
                            !config.dialect.getColumnType(field.getDataType().getType()).equals(column.getType())) {
                        config.dialect.modifyColumn(config, entity.getTableName(), field.getColumnName(), config.dialect.getColumnType(field.getDataType().getType(), field.getLength(), field.getScale()), field.getDisplayName(), false, null);
                    }
                }
            });
        }
    }

    public AppStateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(AppStateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    @Override
    public String toString() {
        return this.getName();
    }


}
