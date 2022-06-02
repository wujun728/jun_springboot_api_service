package com.soukuan.configuration;

import com.soukuan.support.GeneratorSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Title
 * Time 2017/5/31.
 * Version v1.0
 */
@Component
public class ConstantProperties {

    /**
     *  CLOUD : SPRING CLOUD项目
     *  BOOT : SPRING BOOT项目
     */
    public static String CLOUD_OR_BOOT = "CLOUD";
    /**
     * 作者
     */
    public static String AUTHOR = "xiebiao";
    /**
     * 工作路径
     */
    public static String WORK_PATH = "D:/MyProject/github/soukuan";
    /**
     * 项目名称
     */
    public static String PROJECT_NAME = "soukuan-user-center";
    /**
     * 项目名称驼峰（首字母大写）
     */
    public static String PROJECT_NAME_UAPPER;
    /**
     * 项目名简写
     */
    public static String PROJECT;

    @Value("${soukuan.project.type}")
    public void setCloudOrBoot(String cloudOrBoot) {
        CLOUD_OR_BOOT = cloudOrBoot;
    }

    @Value("${soukuan.author}")
    public void setAUTHOR(String AUTHOR) {
        ConstantProperties.AUTHOR = AUTHOR;
    }

    @Value("${soukuan.work.path}")
    public void setWorkPath(String workPath) {
        WORK_PATH = workPath;
    }

    @Value("${soukuan.project.name}")
    public void setProjectName(String projectName) {
        PROJECT_NAME = projectName;
        PROJECT = GeneratorSupport.getProjectPom(projectName);
        PROJECT_NAME_UAPPER = GeneratorSupport.getProjectConvertUpperCamel(projectName);
    }

    /**
     * 项目在硬盘上的基础路径
     */
    //public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String PROJECT_PATH = WORK_PATH + "/" + PROJECT_NAME ;
    public static final String API_PROJECT_PATH = PROJECT_PATH + "/" +PROJECT_NAME+"-api";
    public static final String WEB_PROJECT_PATH = PROJECT_PATH + "/" +PROJECT_NAME+"-web";
    /**
     * java文件路径
     */
    public static final String API_JAVA_PATH = API_PROJECT_PATH + "/src/main/java";
    public static final String WEB_JAVA_PATH = WEB_PROJECT_PATH + "/src/main/java";
    /**
     * 配置文件路径
     */
    public static final String WEB_RESOURCES_PATH = WEB_PROJECT_PATH + "/src/main/resources/config";
    /**
     * MAPPER-XML路径
     */
    public static final String WEB_MAPPER_PATH = WEB_PROJECT_PATH + "/src/main/resources/mapper";
    /**
     * 模板位置
     */
    public static final String TEMPLATE_FILE_PATH =  "src/main/resources/component";
    /**
     * 当前系统日期
     */
    public static final String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    //项目相关路径（可根据需要修改）
    public static final String BASE_PACKAGE = "com.soukuan";
    public static final String API_PACKAGE = BASE_PACKAGE + ".api";//Model所在包
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".domains.po";//Model所在包
    public static final String CONDITION_PACKAGE = BASE_PACKAGE + ".domains.condition";//Condition所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";//Mapper所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";//Controller所在包
    //生成文件的存放路径
    public static final String APPLICATION_PATH_API = GeneratorSupport.packageConvertPath(BASE_PACKAGE);//生成的APPLICATION存放路径
    public static final String PACKAGE_PATH_API = GeneratorSupport.packageConvertPath(API_PACKAGE);//生成的API存放路径
    public static final String PACKAGE_PATH_MODEL = GeneratorSupport.packageConvertPath(MODEL_PACKAGE);//生成的Model存放路径
    public static final String PACKAGE_PATH_CONDITION = GeneratorSupport.packageConvertPath(CONDITION_PACKAGE);//生成的Condition存放路径
    public static final String PACKAGE_PATH_MAPPER = GeneratorSupport.packageConvertPath(MAPPER_PACKAGE);//生成的Mapper存放路径
    public static final String PACKAGE_PATH_SERVICE = GeneratorSupport.packageConvertPath(SERVICE_PACKAGE);//生成的Service存放路径
    public static final String PACKAGE_PATH_SERVICE_IMPL = GeneratorSupport.packageConvertPath(SERVICE_IMPL_PACKAGE);//生成的Service实现存放路径
    public static final String PACKAGE_PATH_CONTROLLER = GeneratorSupport.packageConvertPath(CONTROLLER_PACKAGE);//生成的Controller存放路径

}
