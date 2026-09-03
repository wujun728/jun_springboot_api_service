package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.container.C3;
import com.yuyaogc.lowcode.engine.enums.EntityTypeEnum;

import java.util.*;

/**
 * 模型
 */
public class EntityClass extends Entity{

    private EntityTypeEnum entityType;

    private String tableName;

    private String className;

    private String order;
    private String[] parent;
    private Application application;
    private List<EntityClass> mro;
    private Set<String> inheritChildren = new TreeSet<>();
    private List<EntityClass> bases = new ArrayList<>();

    private Map<String, EntityField> fields = new HashMap<>();
    private Map<String, EntityField> pkFields = new HashMap<>();
    private Map<String, LinkedList<EntityMethod>> methods = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, EntityMethod> events = new LinkedHashMap<>();
    private Map<String, EntityMethod> destroys = new LinkedHashMap<>();

    public Collection<EntityMethod> getDestroys(){
        return destroys.values();
    }

    public boolean hasColumnLabel(String name) {
        return fields.containsKey(name);
    }


    public EntityClass() {

    }

    public EntityClass(Application application) {
        this.application = application;
        this.entityType = EntityTypeEnum.DB;
    }

    public Collection<LinkedList<EntityMethod>> getMethods(){
        return methods.values();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public EntityField getField(String name) {
        return fields.get(name);
    }

    public String[] getPrimaryKey() {
        return pkFields.keySet().toArray(new String[0]);
    }

    public Class<?> getColumnType(String key) {
        return null;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String[] getParent() {
        return parent;
    }

    public void setParent(String[] parent) {
        this.parent = parent;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Collection<EntityField> getFields() {
        return fields.values();
    }

    public Collection<EntityField> getPkFields() {
        return pkFields.values();
    }

    public List<EntityClass> getBases() {
        return bases;
    }


    public EntityTypeEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityTypeEnum entityType) {
        this.entityType = entityType;
    }

    public void setBases(List<EntityClass> bases) {
        this.bases = bases;
        mro = C3.calculate(this, bases);
    }


    public LinkedList<EntityMethod> getMethod(String methodName) {
        return methods.get(methodName);
    }

    public void addMethod(String methodName, EntityMethod entityMethod) {
        LinkedList<EntityMethod> entityMethods;
        if (methods.containsKey(methodName)) {
            entityMethods = methods.get(entityMethod.getName());
            entityMethods.add(entityMethod);
        } else {
            entityMethods = new LinkedList<>();
            entityMethods.add(entityMethod);
            methods.put(methodName, entityMethods);
        }
    }


    public EntityMethod getEvent(String key) {
        return events.get(key);
    }

    public Collection<EntityMethod> getEvents() {
        return events.values();
    }

    public void putEvent(String name,EntityMethod method){
        events.put(name, method);
    }

    public void putDestroy(String name, EntityMethod method){
        destroys.put(name, method);
    }


    public Collection<Service> getService() {
        return services.values();
    }


    public Service getService(String name) {
        return services.get(name);
    }


    public void addField(String name, EntityField field) {
        fields.put(name, field);
    }

    public void addPkField(String name, EntityField field) {
        pkFields.put(name, field);
    }


    public Set<String> getInheritChildren() {
        return inheritChildren;
    }

    public void setInheritChildren(Set<String> inheritChildren) {
        this.inheritChildren = inheritChildren;
    }

    /**
     * 重新计算MRO
     *
     * @return
     */
    public List<EntityClass> resetMro() {
        mro = C3.calculate(this, bases);
        return mro;
    }

    /**
     * 获取MRO（method resolve order）
     *
     * @return
     */
    public List<EntityClass> getMro() {
        if (mro == null) {
            mro = C3.calculate(this, bases);
        }
        return mro;
    }


    @Override
    public String toString() {
        return "EntityClass{" +
                "name='" + this.getName() + '\'' +
                '}';
    }
}
