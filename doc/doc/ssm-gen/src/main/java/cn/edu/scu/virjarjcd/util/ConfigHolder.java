package cn.edu.scu.virjarjcd.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

public class ConfigHolder extends Properties {

    @Getter
    private static boolean lombok = false;
    @Getter
    @Setter
    private static boolean ismerge = false;

    @Getter
    private static boolean singleLayer = false;
    /**
     *
     */
    private static final long serialVersionUID = 4793769832239628L;

    public static ConfigHolder instance = new ConfigHolder();

    private ConfigHolder() {
    }

    public static void calculateData() {
        String sysExportPath = instance.getProperty("sys.exportPath");
        String sysProjectName = instance.getProperty("sys.projectName");
        String sysBasePackage = instance.getProperty("sys.basePackage");
        //String sysBuildTool = instance.getProperty("sys.buildTool");
        Boolean sysLombok = "true".equalsIgnoreCase(instance.getProperty("sys.lombok"));
        if (sysLombok) {
            lombok = true;
        }
        System.out.println(ConfigHolder.instance.getProperty("project.modelLayer"));
        if ("single".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.modelLayer"))) {
            singleLayer = true;
        }

        String sysProjectRootPath = null;
        if (sysExportPath.endsWith("/") || sysExportPath.endsWith("\\")) {
            sysProjectRootPath = sysExportPath + sysProjectName + "/";
        } else {
            sysProjectRootPath = sysExportPath + "/" + sysProjectName + "/";
        }
        instance.setProperty("sys.projectRootPath", sysProjectRootPath);

        String webappRootPath = null;
        String serviceRootPath = null;
        String serviceApiRootPath = null;

        if(instance.getProperty("project.moduleType","single").equals("single")){
            webappRootPath = sysProjectRootPath;
            serviceRootPath = sysProjectRootPath;
            serviceApiRootPath = sysProjectRootPath;

        }else{
            webappRootPath = sysProjectRootPath+"/"+sysProjectName+"-webapp/";
            serviceApiRootPath = sysProjectRootPath+"/"+sysProjectName+"-service-api/";
            serviceRootPath = sysProjectRootPath+"/"+sysProjectName+"-service/";
        }
        instance.setProperty("sys.webappRootPath",webappRootPath);
        instance.setProperty("sys.serviceApiRootPath",serviceApiRootPath);
        instance.setProperty("sys.serviceRootPath",serviceRootPath);

//        String javaSourcePath = sysProjectRootPath + "src/main/java/";
//        String testSourcePath = sysProjectRootPath + "src/test/java/";
//        String resourcePath = sysProjectRootPath + "src/main/resources/";
//        String testResourcePath = sysProjectRootPath + "src/test/resources/";
//        instance.setProperty("sys.javasourcepath", javaSourcePath);
//        instance.setProperty("sys.testsourcepath", testSourcePath);
//        instance.setProperty("sys.resourcepath", resourcePath);
//        instance.setProperty("sys.testresourcepath", testResourcePath);
        String webappJavaSourcePath = webappRootPath +"src/main/java/";
        String webappTestSourcepath = webappRootPath +"src/test/java/";
        String webappResourcePath = webappRootPath + "src/main/resources/";
        String webappTestResourcePath = webappRootPath + "src/test/resources/";

        String serviceJavaSourcePath = serviceRootPath +"src/main/java/";
        String serviceTestSourcepath = serviceRootPath +"src/test/java/";
        String serviceResourcePath = serviceRootPath + "src/main/resources/";
        String serviceTestResourcePath = serviceRootPath + "src/test/resources/";

        String serviceApiJavaSourcePath = serviceApiRootPath +"src/main/java/";
        String serviceApiTestSourcepath = serviceApiRootPath +"src/test/java/";
        String serviceApiResourcePath = serviceApiRootPath + "src/main/resources/";
        String serviceApiTestResourcePath = serviceApiRootPath + "src/test/resources/";

        instance.setProperty("sys.webapp.javasourcepath",webappJavaSourcePath);
        instance.setProperty("sys.webapp.testsourcepath",webappTestSourcepath);
        instance.setProperty("sys.webapp.resoucepath",webappResourcePath);
        instance.setProperty("sys.webapp.testresourcepath",webappTestResourcePath);

        instance.setProperty("sys.service.javasourcepath",serviceJavaSourcePath);
        instance.setProperty("sys.service.testsourcepath",serviceTestSourcepath);
        instance.setProperty("sys.service.resoucepath",serviceResourcePath);
        instance.setProperty("sys.service.testresourcepath",serviceTestResourcePath);

        instance.setProperty("sys.serviceApi.javasourcepath",serviceApiJavaSourcePath);
        instance.setProperty("sys.serviceApi.testsourcepath",serviceApiTestSourcepath);
        instance.setProperty("sys.serviceApi.resoucepath",serviceApiResourcePath);
        instance.setProperty("sys.serviceApi.testresourcepath",serviceApiTestResourcePath);


        String entitypackage ;
        if("single".equalsIgnoreCase(instance.getProperty("project.modelLayer"))){
            entitypackage = sysBasePackage + ".model";
        }else {
            entitypackage = sysBasePackage + ".entity";
        }
        instance.setProperty("sys.entitypackage", entitypackage);

        String sqlmapperpackage = "mapper";// sysBasePackage+".mapper";
        String sqlmapperpath = serviceResourcePath;//+ FileUtils.package2path("mapper");
        instance.setProperty("sys.sqlmapperpackage", sqlmapperpackage);
        instance.setProperty("sys.sqlmapperpath", sqlmapperpath);

        String mapperinterfacepackage = sysBasePackage + ".repository";
        instance.setProperty("sys.mapperinterfacepackage", mapperinterfacepackage);
        instance.setProperty("sys.mapperinterfacepath", serviceResourcePath);

        FileUtils.createIfNotExist(webappJavaSourcePath);
        FileUtils.createIfNotExist(webappTestSourcepath);
        FileUtils.createIfNotExist(webappResourcePath);
        FileUtils.createIfNotExist(webappTestResourcePath);

        FileUtils.createIfNotExist(serviceJavaSourcePath);
        FileUtils.createIfNotExist(serviceTestSourcepath);
        FileUtils.createIfNotExist(serviceResourcePath);
        FileUtils.createIfNotExist(serviceTestResourcePath);

        FileUtils.createIfNotExist(serviceApiJavaSourcePath);
        FileUtils.createIfNotExist(serviceApiTestSourcepath);
        FileUtils.createIfNotExist(serviceApiResourcePath);
        FileUtils.createIfNotExist(serviceApiTestResourcePath);

        FileUtils.createIfNotExist(sqlmapperpath);
    }

}
