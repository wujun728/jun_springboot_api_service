package com.yuyaogc.lowcode.engine.context;

import com.yuyaogc.lowcode.engine.cglib.CglibInvocation;
import com.yuyaogc.lowcode.engine.cglib.Invocation;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityMethod;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Config;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.Memory;
import com.yuyaogc.lowcode.engine.util.StringUtil;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 上下文
 */
public class Context implements AutoCloseable {
    private Map<String, Object> arguments;
    private Map<String, Object> result;
    private String tag;
    private String app;
    private String model;
    private String service;
    private Config config;
    private final Long userId;
    private static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();
    static ThreadLocal<Memory> cache = ThreadLocal.withInitial(() -> new Memory());
    private EntityClass entityClass;


    public String getTag() {
        return tag;
    }

    public String getApp() {
        return app;
    }

    public String getModel() {
        return model;
    }

    public String getService() {
        return service;
    }

    public Long getUserId() {
        return userId;
    }

    public void setContextThreadLocal() {
        contextThreadLocal.set(this);
    }

    public static Context getInstance() {
        return contextThreadLocal.get();
    }

    public Context(Long userId, Config config) {
        if (Objects.isNull(userId)) {
            userId = 1L;
        }
        this.userId = userId;
        this.config = config;
        setContextThreadLocal();
    }

    public Map<String, Object> getResult() {
        if (result == null) {
            result = new HashMap<>(0);
        }
        return result;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public EntityClass getEntity() {
        EntityClass entityClass = getApp(this.app).getEntity(this.model);
        if (!Objects.isNull(entityClass)) {
            return entityClass;
        }
        entityClass = Container.me().getEntityClass(this.model);
        if(!Objects.isNull(entityClass)){
            return entityClass;
        }

        throw new EngineException(String.format("Application %s EntityClass %s is null", this.app, this.model));
    }



    public Model findRef(String xmlid) {
        return get("base.base_mode_data").call("findRef", xmlid);
    }

    public Context get(String model) {
        if (StringUtil.isEmpty(model)) {
            throw new EngineException("模型名不能为空");
        }
        this.model = model;
        String[] names = model.split("\\.");
        if(names.length >1){
            this.app = names[0];
            this.model = names[1];
        }
        this.entityClass = getEntity();
        return this;
    }


    public Application getApp(String appName) {
        return Container.me().get(appName);
    }


    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void setParams(@NotNull Map<String, Object> params) {
        this.model = (String) params.get("model");
        this.service = (String) params.get("service");
        this.app = (String) params.getOrDefault("app", "base");
        this.tag = (String) params.getOrDefault("tag", "master");
        this.arguments = (Map<String, Object>) params.get("args");
    }

    public Map<String, Object> getArgs() {
        return this.arguments;
    }

    public Memory getCache() {
        return cache.get();
    }

    @Override
    public void close() throws Exception {
        contextThreadLocal.remove();
        cache.remove();
    }

    /**
     * api调用
     *
     * @return
     */
    public Object call() throws Exception {
        if(Objects.isNull(entityClass)){
            entityClass = getEntity();
        }

        LinkedList<EntityMethod> methods = entityClass.getMethod(this.service);
        if (Objects.isNull(methods)) {
            throw new EngineException(String.format("模型%s服务%s", this.model, this.service));
        }
        EntityMethod entityMethod = methods.get(0);

        Invocation invocation = new CglibInvocation();

        return getResult().put("data", invocation.invoke2(entityMethod.getMethod(),getArgs()));
    }

    /**
     * 跨应用调用方法
     *
     * @param method 方法名
     * @param args   参数
     * @return 返回值
     */
    public <T> T call(String method, Object... args) {
        LinkedList<EntityMethod> methods = entityClass.getMethod(method);
        if (Objects.isNull(methods)) {
            throw new EngineException(String.format("模型%s服务%s", this.model, this.service));
        }
        EntityMethod entityMethod = methods.get(0);
        this.service = method;

        Invocation invocation = new CglibInvocation();

        Method method1 = entityMethod.getMethod();
        return invocation.invoke(method1,args);
    }

    /**
     *
     * @param criteria
     * @param offset
     * @param limit
     * @param order
     * @return
     * @param <T>
     */
    public <T> List<T> search(Criteria criteria, Integer offset, Integer limit, String order){
        return call("search", criteria, offset, limit, order);
    }


    public void deleteById(Long id){
        call("deleteById", id);
    }

    /**
     *
     * @param criteria
     * @return
     */
    public long count(Criteria criteria){
        return call("count", criteria);
    }


    /**
     *
     * @param criteria
     * @return
     * @param <T>
     */
    public <T> T selectOne(Criteria criteria){
        return call("selectOne", criteria);
    }

    /**
     *
     * @param model
     * @param <T>
     */
    public <T extends Model> void create(T  model){
        call("create", model);
    }


    /**
     *
     * @param model
     * @param <T>
     */
    public <T extends Model> void updateById(T  model){
        call("updateById",model);
    }


}
