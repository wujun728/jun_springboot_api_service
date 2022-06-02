package com.soukuan.support;

import com.soukuan.configuration.ConstantProperties;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class GeneratorSupport {

    /**
     * 将包路径.转化成/
     *
     * @param packageName
     * @return
     */
    public static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    /**
     * 首字母大写驼峰
     *
     * @param tableName
     * @return
     */
    public static String convertUpperCamel(String tableName) {
        StringBuilder result = new StringBuilder();
        String[] tableNames = tableName.split("_");
        for (String table : tableNames) {
            result.append(table.substring(0, 1).toUpperCase() + table.substring(1));
        }
        return result.toString();
    }

    /**
     * 首字母小写驼峰
     *
     * @param tableName
     * @return
     */
    public static String convertLowerCamel(String tableName) {
        String convertUpperCamel = convertUpperCamel(tableName);
        StringBuilder result = new StringBuilder();
        result.append(convertUpperCamel.substring(0, 1).toLowerCase() + convertUpperCamel.substring(1));
        return result.toString();
    }

    public static Configuration getConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(ConstantProperties.TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }


    public static String getProjectConvertUpperCamel(String projectName) {
        StringBuilder result = new StringBuilder();
        String[] projectNames = projectName.split("-");
        for (String project : projectNames) {
            result.append(project.substring(0, 1).toUpperCase() + project.substring(1));
        }
        return result.toString();
    }

    public static String getProjectPom(String projectName) {
        StringBuilder result = new StringBuilder();
        String[] projectNames = projectName.split("-");
        if(projectNames.length > 1){
            for (int i = 0; i < projectNames.length-1; i++) {
                result.append(projectNames[i] + "-");
            }
        }else{
            result.append(projectNames[0] + "-");
        }
        result = result.replace(result.lastIndexOf("-"),result.length(),"");
        return result.toString();
    }

    public static void main(String[] arg) {
        System.out.println(GeneratorSupport.getProjectPom("city-bill-service"));
    }

}
