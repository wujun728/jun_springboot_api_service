package com.yuyaogc.lowcode.engine.loader;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.container.Graph;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Criteria;
import com.yuyaogc.lowcode.engine.entity.Application;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.ClassBuilder;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Db;
import com.yuyaogc.lowcode.engine.plugin.activerecord.Model;
import com.yuyaogc.lowcode.engine.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 应用加载
 * Created by bzGhost on 20230322.
 */
public class SdkLoader extends Loader {

    public static Logger logger = LoggerFactory.getLogger(SdkLoader.class);


    @Override
    public void up() throws Exception {
        try (Context context = new Context(null, Db.getConfig())) {

            this.doInstalls(Collections.singletonList("base-1.0-SNAPSHOT.jar"));

            List<String> jarUrlList =   this.getModuleAlls();

            logger.info("======================================{}",jarUrlList);

            this.doInstalls(jarUrlList);

        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public void install(String fileName, String basePackage, Container container, Application application, Context context) {
        // TODO 找到旧的类加载器，停止端口
        //TODO
        try {
            this.doInstalls(Collections.singletonList(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
