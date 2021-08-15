package cn.edu.scu.virjarjcd.feature.utils.exception;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;
import cn.edu.scu.virjarjcd.util.LombokErasure;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectExceptionPlugin extends MergePluginAdpter {

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        // TODO Auto-generated method stub
        String classPackage = ConfigHolder.instance.getProperty("core.exception.package", "com.virjar.core.exception");
        String path = ConfigHolder.instance.getProperty("core.exception.path");
        String restpackage = ConfigHolder.instance.getProperty("responseEnvelpe.package");

        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        genClassTemplate(classPackage, path, "BeanValidationException", answer);
        genClassTemplate(classPackage, path, "BusinessException", answer);
        genClassTemplate(classPackage, path, "LocalizableBusinessException", answer);
        genClassTemplate(classPackage, path, "RestApiError", answer);
        genRestApiResponseEntityExceptionHandler(classPackage, path, "RestApiResponseEntityExceptionHandler", answer);
        genClassTemplate(classPackage, path, "ValidationError", answer);
        genClassTemplate(classPackage, path, "ValidationException", answer);
        //List<String> globalExceptionHandlerImports = new ArrayList<>();
        //globalExceptionHandlerImports.add(restpackage + ".ResponseEnvelope");
        //genClassTemplate(classPackage, path, "GlobalExceptionHandler", answer,globalExceptionHandlerImports);
        //jalopy存在bug，不能对这个文件进行格式化，强制设置格式化器为mybatis
        genGlobalExceptionHandler(classPackage,path,"GlobalExceptionHandler",answer);
        genGlobalExceptionHandler(classPackage,path,"BadRequestExceptionHandler",answer);
        genClassTemplate(classPackage,path,"ServerInternalException",answer);
        return answer;
    }

    /**
     * jalopy存在bug，不能对这个文件进行格式化，强制设置格式化器为mybatis
     * @param classPackage
     * @param path
     * @param fileName
     * @param answer
     */
    private void genGlobalExceptionHandler(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer){
        try {
            TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(ProjectExceptionPlugin.class.getResourceAsStream(fileName), classPackage);
            String restpackage = ConfigHolder.instance.getProperty("responseEnvelpe.package");
            mapClassScanner.addImportedType(restpackage + ".ResponseEnvelope");
            if (!ConfigHolder.isLombok()) {
                new LombokErasure(mapClassScanner).erasure();
            }
            GeneratedJavaFile gjf =
                    new GeneratedJavaFile(
                            mapClassScanner
                            , path
                            , context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING)
                            , (JavaFormatter)ObjectFactory.createInternalObject(DefaultJavaFormatter.class.getName()));
            answer.add(gjf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void genRestApiResponseEntityExceptionHandler(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            String restpackage = ConfigHolder.instance.getProperty("responseEnvelpe.package");

            TopLevelClass toplevelClass = new JDTCodeParser().parseClass(ProjectExceptionPlugin.class.getResourceAsStream(fileName), classPackage);
            //toplevelClass.addImportedType(restpackage + ".RestApiController");
            toplevelClass.addImportedType(restpackage + ".ResponseEnvelope");
            if (!ConfigHolder.isLombok()) {
                new LombokErasure(toplevelClass).erasure();
            }
            GeneratedJavaFile gjf = new GeneratedJavaFile(toplevelClass, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void genClassTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer,List<String> imports) {
        try {
            TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(ProjectExceptionPlugin.class.getResourceAsStream(fileName), classPackage);
            if(imports != null){
                for(String importz:imports) {
                    mapClassScanner.addImportedType(importz);
                }
            }
            if (!ConfigHolder.isLombok()) {
                new LombokErasure(mapClassScanner).erasure();
            }
            GeneratedJavaFile gjf = new GeneratedJavaFile(mapClassScanner, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void genClassTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
       genClassTemplate(classPackage, path, fileName, answer,null);
    }

    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        ConfigHolder.instance.setProperty("core.exception.package", ConfigHolder.instance.getProperty("sys.basePackage") + ".core.exception");
        ConfigHolder.instance.setProperty("core.exception.path", ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath"));
        return true;
    }
}
