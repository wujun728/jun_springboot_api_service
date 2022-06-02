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
public class PropertiesGenerator {

    public void generatorProperties(Configuration cfg) {
        String projectNameUapper = ConstantProperties.PROJECT_NAME_UAPPER;
        log.info(">>>>>>>>>>>>>生成Properties：{}开始>>>>>>>>>>>>>>>>>>", projectNameUapper);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", ConstantProperties.AUTHOR);
            data.put("projectName",ConstantProperties.PROJECT_NAME);
            data.put("projectNameUapper", projectNameUapper);
            String fileName = "/application.properties";
            if("CLOUD".equals(ConstantProperties.CLOUD_OR_BOOT)){
                fileName = "/bootstrap.properties";
            }
            String pathname = ConstantProperties.WEB_RESOURCES_PATH +  fileName;
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if("CLOUD".equals(ConstantProperties.CLOUD_OR_BOOT)){
                cfg.getTemplate("properties.ftl").process(data, new FileWriter(file));
            }else{
                cfg.getTemplate("properties-boot.ftl").process(data, new FileWriter(file));
            }
            log.info(">>>>>>>>>>>>>生成Properties：{}结束>>>>>>>>>>>>>>>>>>", projectNameUapper);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成Properties：{}失败>>>>>>>>>>>>>>>>>>", projectNameUapper);
            throw new RuntimeException("生成Properties失败", e);
        }
    }
}
