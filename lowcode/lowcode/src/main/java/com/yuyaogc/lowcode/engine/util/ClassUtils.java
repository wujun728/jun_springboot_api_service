package com.yuyaogc.lowcode.engine.util;

import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.*;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.entity.*;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.enums.DataTypeEnum;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtils {
    public static Logger logger = LoggerFactory.getLogger(ClassUtils.class);
    private static ClassLoader systemClassLoader;

    static {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ignored) {
            // AccessControlException on Google App Engine
        }
    }

    public static Tuple<APP, List<Class<?>>> scanAppInfo(String basePackage, AppClassLoader jarLauncher){
        Tuple<APP, List<Class<?>>> tuple = null;

        List<Class<?>> classList = ClassUtils.scanPackage(  basePackage, jarLauncher);

        for (Class<?> clazz : classList) {
            if (clazz.isAnnotationPresent(APP.class)) {
                tuple = new Tuple<>(clazz.getAnnotation(APP.class), classList);
            }
        }


        return tuple;
    }



    public static void buildClass(Class<?> entityClass, Application application) {
        Table table = entityClass.getAnnotation(Table.class);
        EntityClass entity = new EntityClass(application);
        if (entityClass.isAnnotationPresent(Table.class)) {
            entity.setParent(table.parent());
            entity.setDisplayName(table.displayName());
            entity.setName(table.name());
            entity.setId(entityClass.getName());
            entity.setClassName(entityClass.getName());
            entity.setTableName(table.name());
            entity.setAppName(application.getName());
            entity.setVersion(application.getVersion());

            buildField(entity, entityClass);
            buildMethod(entity, entityClass);
            application.buildModel(table.name(), entity);
        }
    }

    public static void buildMethod(EntityClass entity, Class<?> entityClass) {
        Method[] methods = entityClass.getDeclaredMethods();

        for (Method method : methods) {
            method.setAccessible(true);
            if (Modifier.isPublic(method.getModifiers())) {
                if (method.isAnnotationPresent(Service.class)) {

                    // 方法
                    EntityMethod entityMethod = new EntityMethod(entity);
                    entityMethod.setMethod(method);
                    entityMethod.setName(method.getName());
                    entityMethod.setApplication(entity.getApplication());
                    entityMethod.setClassName(entityClass.getName());


                    // 服务
                    Service service = method.getAnnotation(Service.class);
                    if (service.event()) {
                        entity.putEvent(entityMethod.getName(), entityMethod);
                    } else if (service.stop()) {
                        entity.putDestroy(entityMethod.getName(), entityMethod);
                    }

                    // 参数
                    entityMethod.setDisplayName(service.displayName());

                    Parameter[] params = method.getParameters();
                    for(Parameter parameter: params){
                        Param param = new Param();
                        param.setName(parameter.getName());
                        param.setParamType(DataType.create(parameter.getType().getSimpleName()));
                        entityMethod.addParam(param);
                    }

                    entity.addMethod(method.getName(), entityMethod);
                }
            }
        }


        if (entityClass.getSuperclass() != null) {
            buildMethod(entity, entityClass.getSuperclass());
        }
    }

    public static Application buildApplication( Application application, List<Class<?>> classList) throws IOException {
        try {
            for (Class<?> clazz : classList) {
                if (clazz.isAnnotationPresent(APP.class)) {
                    application.setAppInfo(clazz.getAnnotation(APP.class));
                }
                buildClass(clazz, application);
            }
            return application;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void loadSeedData(  String appName ){
        Context context = Context.getInstance();

        AppClassLoader classLoader =  Container.me().get(appName).getAppClassLoader();

        Enumeration<JarEntry> entries = classLoader.jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if(name.endsWith(".xml")){
                InputStream input =null;
                try {
                    input = classLoader.jarFile.getInputStream(jarEntry);
                } catch (Exception e){}

                ImportUtils.importXml(input, appName, context, (m, e) -> {
                    logger.warn(m, e);
                });
            } else if(name.endsWith(".json")){
                InputStream input =null;
                try {
                    input = classLoader.jarFile.getInputStream(jarEntry);
                } catch (Exception e){}

                ImportUtils.importJson(input,appName,  context);

            }
        }
    }



    public static List<Class<?>> scanPackage( String basePackage, AppClassLoader classLoader) {
        List<Class<?>> result = new ArrayList<>();
        Enumeration<JarEntry> entries = classLoader.jarFile.entries();
        String basePackage1 = basePackage.replaceAll("\\.", "/");
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.contains(basePackage1) && name.startsWith("BOOT-INF/classes") && name.endsWith(".class")) {
                String className = name.replace("BOOT-INF/classes/", "").
                        replace("\\", ".").
                        replace("/", ".").
                        replace(".class", "");
                try {
                    Class clazz = classLoader.loadClass(className);
                    if (clazz.isAnnotationPresent(Table.class)
                            || clazz.isAnnotationPresent(APP.class)) {
                        result.add(classLoader.loadClass(className));
                    }



                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(String.format("实例化对象时出现错误,请尝试给 %s 添加无参的构造方法", clazz.getName()), e);
        }
    }

    private static Class<?> getTargetClass(Class<?> clazz) {
        boolean isCglibProxy = clazz.getName().contains("$$");
        return isCglibProxy ? clazz.getSuperclass() : clazz;
    }


    public static String getName(Class<?> clazz) {
        Class<?> class1 = getTargetClass(clazz);
        return class1.getSimpleName();
    }


    public static void buildField(EntityClass entity, Class<?> entityClass) {
        List<EntityField> fields = _getFields(entity, entityClass, null, null);
    }


    public static void addValidate(Validate validate, java.lang.reflect.Field field) {
        if (field.isAnnotationPresent(NotBlank.class)) {
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            validate.setEmpty(false);
            validate.setMessage(notBlank.message());
        }
        if (field.isAnnotationPresent(Max.class)) {
            Max max = field.getAnnotation(Max.class);
            validate.setMax(max.value());
        }
        if (field.isAnnotationPresent(Min.class)) {
            Min min = field.getAnnotation(Min.class);
            validate.setMin(min.value());
        }
        if (field.isAnnotationPresent(Pattern.class)) {
            Pattern pattern = field.getAnnotation(Pattern.class);
            validate.setRegexp(pattern.regexp());
        }
        validate.getField().addValidate(validate);
    }


    private Map<String, Class<?>> _getGenericTypeMap(Class<?> entityClass) {
        Map<String, Class<?>> genericMap = new HashMap<String, Class<?>>();
        if (entityClass == Object.class) {
            return genericMap;
        }
        //获取父类和泛型信息
        Class<?> superClass = entityClass.getSuperclass();
        //判断superClass
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Table.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            if (entityClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments();
                TypeVariable[] typeVariables = superClass.getTypeParameters();
                if (typeVariables.length > 0) {
                    for (int i = 0; i < typeVariables.length; i++) {
                        if (types[i] instanceof Class) {
                            genericMap.put(typeVariables[i].getName(), (Class<?>) types[i]);
                        }
                    }
                }
            }
            genericMap.putAll(_getGenericTypeMap(superClass));
        }
        return genericMap;
    }

    public static List<PropertyDescriptor> getProperties(Class<?> entityClass) {
        List<PropertyDescriptor> entityFields = new ArrayList<PropertyDescriptor>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(entityClass);
        } catch (IntrospectionException e) {
            throw new EngineException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            if (!desc.getName().equals("class")) {
                entityFields.add(desc);
            }
        }
        return entityFields;
    }

    public static List<EntityField> _getFields(EntityClass entity, Class<?> entityClass, List<EntityField> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        if (level == null) {
            level = 0;
        }
        java.lang.reflect.Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            java.lang.reflect.Field field = fields[i];
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                String columnName = StringUtils.camelToUnderline(field.getName());
                String displayName = "";
                Integer length = null;
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    columnName = StringUtils.camelToUnderline(column.name());
                    length = column.length();
                    displayName = column.label();
                }
                EntityField entityField = new EntityField(entity);
                entityField.setStore(true);
                entityField.setLength(length);
                entityField.setDisplayName(displayName);

                DataType dataType = null;

                Class<?> typeClass;


                if(field.isAnnotationPresent(File.class)){
                    File file = field.getAnnotation(File.class);
                    entityField.setDisplayName(file.label());
                    entityField.setName(file.name());
                    entityField.getAttrs().put("accept",file.accept());
                    entityField.getAttrs().put("receiver",file.receiver());
                    dataType = DataType.create(Constants.FILE);
                }else if(field.isAnnotationPresent(Selection.class)){
                    Selection selection = field.getAnnotation(Selection.class);
                    List<Tuple<String,String>> tupleList =new ArrayList<>();
                    for(Option option: selection.options()){
                        tupleList.add(new Tuple<>(option.label(), option.value()));
                    }
                    entityField.getAttrs().put(Constants.SELECTION, tupleList);
                    dataType = DataType.create(Constants.SELECTION);
                } else if(field.isAnnotationPresent(Dict.class)){
                    Dict dict = field.getAnnotation(Dict.class);
                    entityField.setRelModel(dict.model());
                    entityField.setDisplayName(dict.label());
                    entityField.getAttrs().put("typeCode", dict.typeCode());
                    dataType = DataType.create(Constants.DICT);
                } else if (field.isAnnotationPresent(ManyToOne.class)) {
                    typeClass = field.getType();
                    Table table =  typeClass.getAnnotation(Table.class);
                    entityField.setRelModel(table.name());
                    dataType = DataType.create(Constants.MANY2ONE);
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        columnName = joinColumn.name();
                        entityField.setReferencedProperty(joinColumn.referencedColumnName());
                    }
                    //TODO MAP CLASS
                } else if (field.isAnnotationPresent(ManyToMany.class)) {
                    ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);

                    dataType = DataType.create(Constants.MANY2MANY);
                    typeClass = BeanUtils.getTypeClass(field);
                    entityField.setStore(false);
                    Table table =  typeClass.getAnnotation(Table.class);
                    entityField.setRelModel2(table.name());


                    if(StringUtils.isNotEmpty(manyToMany.mappedBy())){
                        // 非维护方
                        try {
                          Field  field1 = typeClass.getDeclaredField(manyToMany.mappedBy());
                            JoinTable inversejoinTable   =   field1.getAnnotation(JoinTable.class);
                            for(JoinColumn inverseColumn : inversejoinTable.inverseJoinColumns()){
                                entityField.setRelModel(inversejoinTable.name());
                                entityField.setInverseName(inverseColumn.name());
                                entityField.setReferencedProperty(inverseColumn.referencedColumnName());
                            }

                            for(JoinColumn joinColumn: inversejoinTable.joinColumns()){
                                entityField.setJoinColumnName(joinColumn.name());
                            }
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    JoinTable    joinTable = field.getAnnotation(JoinTable.class);
                    if(joinTable != null){
                        //维护方
                        EntityClass relModel = new EntityClass();
                        relModel.setName(joinTable.name());
                        relModel.setTableName(joinTable.name());


                        entityField.setRelModel(joinTable.name());

                        for(JoinColumn joinColumn : joinTable.joinColumns()){

                            entityField.setJoinColumnName(joinColumn.name());

                            EntityField relField = new EntityField();
                            relField.setDataType(DataType.create(Constants.LONG));
                            relField.setName(joinColumn.name());
                            relField.setColumnName(joinColumn.name());
                            relModel.addField(relField.getName(), relField);
                        }

                        for(JoinColumn inverseColumn: joinTable.inverseJoinColumns()){

                            entityField.setRelModel(joinTable.name());
                            entityField.setInverseName(inverseColumn.name());


                            EntityField relField = new EntityField();
                            relField.setDataType(DataType.create(Constants.LONG));
                            relField.setName(inverseColumn.name());
                            relField.setColumnName(inverseColumn.name());
                            relModel.addField(relField.getName(), relField);
                        }


                        EntityField relField = new EntityField();
                        relField.setDataType(DataType.create(Constants.LONG));
                        relField.setName("id");
                        relField.setColumnName("id");
                        relField.setPk(true);
                        relModel.addField(relField.getName(), relField);
                        relModel.addPkField(relField.getName(), relField);

                        entity.getApplication().buildModel(relModel.getName(), relModel);
                    }


                } else if (field.isAnnotationPresent(OneToMany.class)) {

                    dataType = DataType.create(Constants.ONE2MANY);
                    typeClass = BeanUtils.getTypeClass(field);
                    Table table =  typeClass.getAnnotation(Table.class);
                    entityField.setRelModel(table.name());
                    entityField.setStore(false);
                } else if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (column.type().equals(DataTypeEnum.TEXT)) {
                        dataType = DataType.create(Constants.TEXT);
                    } else if(column.type().equals(DataTypeEnum.FILE)){
                        dataType = DataType.create(Constants.FILE);
                    } else {
                        dataType = DataType.create(field.getType().getSimpleName());
                    }
                } else {
                    dataType = DataType.create(field.getType().getSimpleName());
                }
                entityField.setDataType(dataType);
                if (field.isAnnotationPresent(Id.class)) {
                    entityField.setPk(true);
                    entityField.setDataType(DataType.create(Constants.LONG));
                    entity.addPkField(field.getName(), entityField);
                }
                entityField.setName(field.getName());
                if (StringUtil.isEmpty(columnName)) {
                    columnName = field.getName();
                }
                entityField.setColumnName(columnName);
                if (level.intValue() != 0) {
                    fieldList.add(index, entityField);
                    entity.addField(field.getName(), entityField);
                    index++;
                } else {
                    fieldList.add(entityField);
                    entity.addField(field.getName(), entityField);
                }

                if (field.isAnnotationPresent(Column.class)) {
                    Validate validate = new Validate();
                    validate.setField(entityField);
                    addValidate(validate, field);
                }

            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Table.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return _getFields(entity, entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }


    /**
     * @param name
     * @param classLoader
     * @return
     * @since 3.4.3
     */
    public static Class<?> toClassConfident(String name, ClassLoader classLoader) {
        try {
            return loadClass(name, getClassLoaders(classLoader));
        } catch (ClassNotFoundException e) {
            throw new EngineException("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
        }
    }

    private static Class<?> loadClass(String className, ClassLoader[] classLoaders) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            if (classLoader != null) {
                try {
                    return Class.forName(className, true, classLoader);
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
        throw new ClassNotFoundException("Cannot find class: " + className);
    }

    private static ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{
                classLoader,
                Thread.currentThread().getContextClassLoader(),
                ClassUtils.class.getClassLoader(),
                systemClassLoader};
    }

}
