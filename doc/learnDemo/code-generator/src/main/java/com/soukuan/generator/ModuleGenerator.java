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
public class ModuleGenerator {

    public void generatorModule(Configuration cfg, TableProperties tableProperties) {
        String modelNameUpperCamel = tableProperties.getModelNameUpperCamel();
        log.info(">>>>>>>>>>>>>生成module:{}开始>>>>>>>>>>>>>>>>>>",modelNameUpperCamel);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", tableProperties.getAuthor());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableProperties.getModelNameLowerCamel());
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            data.put("tableName",tableProperties.getTableName());
            data.put("columnNameList",tableProperties.getColumnNameList());
            data.put("fieldNameList",tableProperties.getFieldNameList());
            data.put("fieldTypeList",tableProperties.getFieldTypeList());
            data.put("columnComments",tableProperties.getColumnComments());
            //生成对象
            String pathname = ConstantProperties.API_JAVA_PATH + ConstantProperties.PACKAGE_PATH_MODEL + modelNameUpperCamel + ".java";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("module.ftl").process(data, new FileWriter(file));
            //生成condition
            pathname = ConstantProperties.API_JAVA_PATH + ConstantProperties.PACKAGE_PATH_CONDITION + modelNameUpperCamel + "Condition.java";
            file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("module-condition.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成module：{}结束>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成module：{}失败>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            throw new RuntimeException("生成module失败", e);
        }
    }
}
