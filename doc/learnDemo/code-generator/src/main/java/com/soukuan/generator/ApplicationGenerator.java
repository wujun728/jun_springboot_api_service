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
public class ApplicationGenerator {

    public void generatorApplication(Configuration cfg) {
        String projectNameUapper = ConstantProperties.PROJECT_NAME_UAPPER;
        log.info(">>>>>>>>>>>>>生成Application：{}开始>>>>>>>>>>>>>>>>>>", projectNameUapper);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", ConstantProperties.AUTHOR);
            data.put("projectName",ConstantProperties.PROJECT_NAME);
            data.put("projectNameUapper", projectNameUapper);
            String pathname = ConstantProperties.WEB_JAVA_PATH + ConstantProperties.APPLICATION_PATH_API + projectNameUapper + "Application.java";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("application.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成Application：{}结束>>>>>>>>>>>>>>>>>>", projectNameUapper);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成Application：{}失败>>>>>>>>>>>>>>>>>>", projectNameUapper);
            throw new RuntimeException("生成Application失败", e);
        }
    }
}
