package com.soukuan.handler;

import com.soukuan.configuration.ConstantProperties;
import com.soukuan.domain.TableProperties;
import com.soukuan.generator.*;
import com.soukuan.support.DatabaseUtil;
import com.soukuan.support.GeneratorSupport;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CodeHandler {

    @Resource
    private ApiGenerator apiGenerator;
    @Resource
    private ControllerGenerator controllerGenerator;
    @Resource
    private ServiceGenerator serviceGenerator;
    @Resource
    private MapperGenerator mapperGenerator;
    @Resource
    private ModuleGenerator moduleGenerator;
    @Resource
    private MapperXmlGenerator mapperXmlGenerator;

    @Resource
    private ApplicationGenerator applicationGenerator;
    @Resource
    private PropertiesGenerator propertiesGenerator;
    @Resource
    private PomGenerator pomGenerator;

    public void codeGeneratorHandler(String tableNames) {
        if (!StringUtils.isEmpty(tableNames)) {
            //1.设置公共参数
            TableProperties tableProperties = new TableProperties();
            tableProperties.setAuthor(ConstantProperties.AUTHOR);
            if ("*".equals(tableNames)) {
                //2.1 新建项目|生成表对应文件
                List<String> tableList = DatabaseUtil.getTableNames();
                tableList.forEach(tableName -> {
                    tableProperties.setTableName(tableName);
                    generatorCodeTable(tableProperties);
                });
                //2.2 新建项目|单独生成pom、properties、application
                try {
                    Configuration cfg = GeneratorSupport.getConfiguration();
                    applicationGenerator.generatorApplication(cfg);
                    propertiesGenerator.generatorProperties(cfg);
                    pomGenerator.generatorPom(cfg);
                } catch (IOException e) {
                    log.error("代码生成失败！", e);
                }
            } else {
                //2.1 修改项目|单独表
                String[] tables = tableNames.split(",");
                for (String tableName : tables) {
                    tableProperties.setTableName(tableName);
                    generatorCodeTable(tableProperties);
                }
            }
        } else {
            throw new RuntimeException("未设置tableName");
        }
    }

    private void generatorCodeTable(TableProperties tableProperties) {
        String tableName = tableProperties.getTableName();
        //设置表字段相关属性
        tableProperties.setColumnNameList(DatabaseUtil.getColumnNames(tableName));
        tableProperties.setColumnTypeList(DatabaseUtil.getColumnTypes(tableName));
        tableProperties.setColumnComments(DatabaseUtil.getColumnComments(tableName));
        if(tableName.startsWith("t_")){
            tableName = tableName.replaceFirst("t_","");
        }
        if(tableName.startsWith("tb_")){
            tableName = tableName.replaceFirst("tb_","");
        }
        if(tableName.startsWith("sys_")){
            tableName = tableName.replaceFirst("sys_","");
        }
        String modelNameUpperCamel = GeneratorSupport.convertUpperCamel(tableName);
        String modelNameLowerCamel = GeneratorSupport.convertLowerCamel(tableName);
        tableProperties.setModelNameLowerCamel(modelNameLowerCamel);
        tableProperties.setModelNameUpperCamel(modelNameUpperCamel);
        try {
            Configuration cfg = GeneratorSupport.getConfiguration();
            apiGenerator.generatorApi(cfg, tableProperties);
            moduleGenerator.generatorModule(cfg, tableProperties);
            controllerGenerator.generatorController(cfg, tableProperties);
            serviceGenerator.generatorService(cfg, tableProperties);
            mapperGenerator.generatorMapper(cfg, tableProperties);
            mapperXmlGenerator.generatorMapperXml(cfg, tableProperties);
        } catch (IOException e) {
            log.error("代码生成失败！" + modelNameLowerCamel, e);
        }
    }
}
