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
public class ServiceGenerator {

    public void generatorService(Configuration cfg, TableProperties tableProperties) {
        String modelNameUpperCamel = tableProperties.getModelNameUpperCamel();
        try {
            //1.生成Service
            log.info(">>>>>>>>>>>>>生成Service：{}开始>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            Map<String, Object> data = new HashMap<>();
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", tableProperties.getAuthor());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableProperties.getModelNameLowerCamel());
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            String pathname = ConstantProperties.WEB_JAVA_PATH + ConstantProperties.PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成Service：{}结束>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);

            //2.生成ServiceImpl
            log.info(">>>>>>>>>>>>>生成ServiceImpl：{}开始>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            File file1 = new File(ConstantProperties.WEB_JAVA_PATH + ConstantProperties.PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data, new FileWriter(file1));
            log.info(">>>>>>>>>>>>>生成ServiceImpl：{}结束>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成Service：{}失败>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            throw new RuntimeException("生成Service失败", e);
        }
    }
}
