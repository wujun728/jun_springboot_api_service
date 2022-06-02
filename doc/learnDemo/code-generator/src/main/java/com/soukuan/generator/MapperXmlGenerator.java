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
public class MapperXmlGenerator {
    public void  generatorMapperXml(Configuration cfg, TableProperties tableProperties) {
        String modelNameUpperCamel = tableProperties.getModelNameUpperCamel();
        log.info(">>>>>>>>>>>>>生成MapperXml:{}开始>>>>>>>>>>>>>>>>>>",modelNameUpperCamel);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("date", ConstantProperties.NOW_DATE);
            data.put("author", tableProperties.getAuthor());
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableProperties.getModelNameLowerCamel());
            data.put("basePackage", ConstantProperties.BASE_PACKAGE);
            data.put("mapperPackage",ConstantProperties.MAPPER_PACKAGE);
            data.put("modelPackage",ConstantProperties.MODEL_PACKAGE);
            data.put("tableName",tableProperties.getTableName());
            data.put("columnNameList",tableProperties.getColumnNameList());
            data.put("fieldNameList",tableProperties.getFieldNameList());
            data.put("fieldTypeList",tableProperties.getFieldTypeList());
            data.put("jdbcTypeList",tableProperties.getJdbcTypeList());
            data.put("columnComments",tableProperties.getColumnComments());
            String pathname = ConstantProperties.WEB_MAPPER_PATH +"/"+ modelNameUpperCamel + ".xml";
            File file = new File(pathname);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("mapper-xml.ftl").process(data, new FileWriter(file));
            log.info(">>>>>>>>>>>>>生成MapperXml：{}结束>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>生成MapperXml：{}失败>>>>>>>>>>>>>>>>>>", modelNameUpperCamel);
            throw new RuntimeException("生成MapperXml失败", e);
        }
    }
}
