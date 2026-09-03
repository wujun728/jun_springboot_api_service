package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.cglib.Proxy;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.exception.EngineLogger;
import com.yuyaogc.lowcode.engine.loader.AppClassLoader;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.util.BeanUtils;
import com.yuyaogc.lowcode.engine.util.JsonUtil;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.yuyaogc.lowcode.engine.util.BeanUtils.getTypeClass;

/**
 * 模型方法
 */
public class EntityMethod extends Entity {
    private static EngineLogger log = EngineLogger.me(EntityMethod.class);

    private String name;
    private String displayName;
    private EntityClass entity;
    private Application application;
    private String className;
    private Map<String, Param> paramIns = new LinkedHashMap<>();
    private Map<String, Param> paramOuts = new LinkedHashMap<>();

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    private Method method;

    public void setMethod(Method method) {
        this.method = method;
    }

    public EntityMethod() {

    }

    public EntityMethod(EntityClass entity) {
        this.entity = entity;
    }



    public void addParam(Param param){
        paramIns.put(param.getName(), param);
    }

    public Class getClazz() {
        AppClassLoader appClassLoader = this.getApplication().getAppClassLoader();
        try {
            return appClassLoader.loadClass(this.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Method getMethod() {
        if(method == null){
            for (Method method1 : getClazz().getDeclaredMethods()) {
                if (StringUtils.equals(method1.getName(), this.getName())) {
                    method = method1;
                }
            }
        }
        return method;
    }




    public EntityClass getEntity() {
        return entity;
    }

    public void setEntity(EntityClass entity) {
        this.entity = entity;
    }
}
