package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.container.Graph;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.Param;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.IPlugin;
import com.yuyaogc.lowcode.engine.plugin.Plugins;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import com.yuyaogc.lowcode.engine.util.Tuple;
import redis.clients.jedis.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public abstract class Loader {

    private static Loader loader;

    public static Loader getLoader() {
        return loader;
    }

    public static void setLoader(Loader loader1) {
        loader = loader1;
    }


    public void loadPlugin(Plugins plugins) {
        List<IPlugin> pluginList = plugins.getPluginList();
        if (pluginList == null) {
            return;
        }

        for (IPlugin plugin : plugins.getPluginList()) {
            try {
                if (plugin.start() == false) {
                    String message = "Plugin start error: " + plugin.getClass().getName();
                    throw new RuntimeException(message);
                }
            } catch (Exception e) {
                String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
                throw new RuntimeException(message, e);
            }

        }
    }




    public List<String> getALLJarList(List<Model> metaApps,List<Model> dependApps){
        Graph graph = new Graph();

        List<String> missAppList = new ArrayList<>();

        for(Model metaApp: metaApps){
            List<Model> dependList =    dependApps.stream().filter(d->metaApp.getLong("id").equals(d.getLong("baseApp"))).collect(Collectors.toList());
            graph.addEdge(metaApp.getLong("id"), 0L);
            addDepends(dependList, metaApps, graph, metaApp, missAppList);
        }
        List<String> jarList = new ArrayList<>();

        if(!missAppList.isEmpty()){
            throw new EngineException(String.format("缺少依赖: %s",missAppList ));
        }

        if(graph.hasCycle()){
            throw new EngineException(String.format("循环依赖: %s",graph ));
        }


        List<Long> nodes = graph.topologicalSort();
        for(int i = nodes.size()-1; i>=0; i--){
            int finalI = i;
            Optional<Model> optionalModel = metaApps.stream().filter(c -> c.getLong("id").equals(nodes.get(finalI))).findFirst();
            if(optionalModel.isPresent()){
                jarList.add(optionalModel.get().getStr("jarUrl"));
            }
        }
        return jarList;
    }


    public List<String> getModuleAlls(){
        Context context = Context.getInstance();

        List<Model> metaApps =   context.get("base.base_module").search(Criteria.equal("state", 0), 0, 0, null);
        List<Long> metaAppIdList = metaApps.stream().map(model -> model.getLong("id")).collect(Collectors.toList());
        List<Model> dependApps =  context.get("base.base_depends").search(Criteria.in("baseApp", (Object) metaAppIdList), 0, 0, null);

        return getALLJarList(metaApps, dependApps);
    }



    public void addDepends(List<Model> dependList, List<Model> modules, Graph graph, Model module,List<String> missDependList){
        for(Model depend: dependList){
            Optional<Model> optionalModel = modules.stream().filter(c -> c.getStr("appName").equals(depend.getStr("name"))).findFirst();
            if(optionalModel.isPresent()){
                Model deptModule = optionalModel.get();
                graph.addEdge(module.getLong("id"), deptModule.getLong("id"));
            } else {
                missDependList.add(String.format("%s",depend.getStr("name")));
            }
        }
    }

    public APP getAppInfo(String fileName, String basePackage) throws IOException {
        JarFile jarFile = new JarFile(  fileName);
        AppClassLoader jarLauncher = new AppClassLoader(jarFile);
        List<Class<?>> classList = ClassUtils.scanPackage(  basePackage, jarLauncher);
        for (Class<?> clazz : classList) {
            if (clazz.isAnnotationPresent(APP.class)) {
              return   clazz.getAnnotation(APP.class);
            }
        }
        return null;
    }


    /**
     * 构建应用
     * @param fileNameList jar包路径
     * @param applicationList 应用集合
     * @throws IOException
     */
    public void bildApplications(List<String> fileNameList, List<Application> applicationList) throws IOException {

        for(String fileName: fileNameList){

            JarFile jarFile = new JarFile(  fileName);

            AppClassLoader appClassLoader = new AppClassLoader(jarFile);

            Tuple<APP, List<Class<?>>> appTuple =  ClassUtils.scanAppInfo("com.yatop.lambda", appClassLoader);

            Application application = new Application();
            application.setAppInfo(appTuple.getItem1());
            application.setClassList(appTuple.getItem2());
            application.setAppClassLoader(appClassLoader);

            // 添加应用到容器
            Container.me().add(application);

            applicationList.add(application);
        }

    }


    public void doInstalls(List<String> jarUrlList) throws IOException {
        List<Application> applicationList = new ArrayList<>();

        // 构建应用
        this.bildApplications(jarUrlList, applicationList);

        // 安装应用
        for(Application application: applicationList){
            // 构建应用模型
            application.buildClass();
        }


        //构建容器模型继承扩展
        Container.me().buildInherit();


        // 初始化当前需要安装的应用
       for(Application application: applicationList){
           // 初始化表结构
           application.autoTableInit();

           // 加载种子数据
           application.loadSeedData();
       }


        // 启动事件
        this.doEvents(applicationList);

    }


    public void doEvents(List<Application> applicationList){
        for(Application application: applicationList){
            application.onEvent();
        }
    }


    public abstract void up() throws Exception ;


    public abstract void install(String fileName, String basePackage, Container container, Application application, Context context);
}
