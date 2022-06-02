package com.soukuan.generator;

import com.soukuan.configuration.ConstantProperties;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PomGenerator {

    public void generatorPom(Configuration cfg) {
        String projectNameUapper = ConstantProperties.PROJECT_NAME_UAPPER;
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", ConstantProperties.AUTHOR);
            data.put("project",ConstantProperties.PROJECT);
            data.put("projectName",ConstantProperties.PROJECT_NAME);
            data.put("projectNameUapper", projectNameUapper);
            //1.生成父级POM
            log.info(">>>>>>>>>>>>>生成POM:{}开始>>>>>>>>>>>>>>>>>>",projectNameUapper);
            String pathname = ConstantProperties.PROJECT_PATH + "/pom.xml";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("pom.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成POM：{}结束>>>>>>>>>>>>>>>>>>", projectNameUapper);
            //2.生成API-POM
            log.info(">>>>>>>>>>>>>生成API-POM:{}开始>>>>>>>>>>>>>>>>>>",projectNameUapper);
            pathname = ConstantProperties.API_PROJECT_PATH + "/pom.xml";
            file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("pom-api.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成API-POM：{}结束>>>>>>>>>>>>>>>>>>", projectNameUapper);
            //3.生成WEB-POM
            log.info(">>>>>>>>>>>>>生成WEB-POM:{}开始>>>>>>>>>>>>>>>>>>",projectNameUapper);
            pathname = ConstantProperties.WEB_PROJECT_PATH + "/pom.xml";
            file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if("CLOUD".equals(ConstantProperties.CLOUD_OR_BOOT)){
                cfg.getTemplate("pom-web.ftl").process(data, new FileWriter(file));
            }else{
                cfg.getTemplate("pom-web-boot.ftl").process(data, new FileWriter(file));
            }
            log.info(">>>>>>>>>>>>>生成WEB-POM：{}结束>>>>>>>>>>>>>>>>>>", projectNameUapper);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成POM：{}失败>>>>>>>>>>>>>>>>>>", projectNameUapper);
            throw new RuntimeException("生成POM失败", e);
        }
    }
}
