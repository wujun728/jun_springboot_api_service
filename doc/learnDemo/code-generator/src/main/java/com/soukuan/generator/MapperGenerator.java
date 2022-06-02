package com.soukuan.generator;

import com.soukuan.configuration.ConstantProperties;
import com.soukuan.domain.TableProperties;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MapperGenerator {
    public void  generatorMapper(Configuration cfg, TableProperties tableProperties) {
        String modelNameUpperCamel = tableProperties.getModelNameUpperCamel();
        log.info(">>>>>>>>>>>>>生成mapper:{}开始>>>>>>>>>>>>>>>>>>",modelNameUpperCamel);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", tableProperties.getAuthor());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableProperties.getModelNameLowerCamel());
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            String pathname = ConstantProperties.WEB_JAVA_PATH + ConstantProperties.PACKAGE_PATH_MAPPER + modelNameUpperCamel + "Mapper.java";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("mapper.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成mapper：{}结束>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成mapper：{}失败>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            throw new RuntimeException("生成mapper失败", e);
        }
    }
}
