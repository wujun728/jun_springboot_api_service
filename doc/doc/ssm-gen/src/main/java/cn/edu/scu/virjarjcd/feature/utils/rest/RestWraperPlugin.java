package cn.edu.scu.virjarjcd.feature.utils.rest;

import cn.edu.scu.virjarjcd.dom.java.VirjarAnnotation;
import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;
import cn.edu.scu.virjarjcd.util.LombokErasure;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestWraperPlugin extends MergePluginAdpter {

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        // TODO Auto-generated method stub
        //return super.contextGenerateAdditionalJavaFiles();

        String classPackage = ConfigHolder.instance.getProperty("responseEnvelpe.package", "");
        String path = ConfigHolder.instance.getProperty("responseEnvelpe.path", "");
        FileUtils.createIfNotExist(path);
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        genClassTemplate(classPackage, path, "PageInfo", answer);
        genResponseEnvelopTemplate(classPackage, path, "ResponseEnvelope", answer);
        //genClassTemplate(classPackage, path, "RestApiControllerHandlerMapping", answer);

        //genAnnotationTemplate(classPackage, path, "RestApiController", answer);
        return answer;
    }

    private void genResponseEnvelopTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            TopLevelClass topLevelClass = new JDTCodeParser().parseClass(RestWraperPlugin.class.getResourceAsStream(fileName), classPackage);

            String sysExceptionPackage = ConfigHolder.instance.getProperty("core.exception.package");
            topLevelClass.addImportedType(sysExceptionPackage + ".RestApiError");
            if (!ConfigHolder.isLombok()) {
                new LombokErasure(topLevelClass).erasure();
            }
            GeneratedJavaFile gjf = new GeneratedJavaFile(topLevelClass, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void genClassTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(RestWraperPlugin.class.getResourceAsStream(fileName), classPackage);
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

    private void genAnnotationTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            VirjarAnnotation parseAnotation = new JDTCodeParser().parseAnotation(RestWraperPlugin.class.getResourceAsStream(fileName), classPackage);
            GeneratedJavaFile gjf = new GeneratedJavaFile(parseAnotation, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        ConfigHolder.instance.setProperty("responseEnvelpe.package", ConfigHolder.instance.getProperty("sys.basePackage") + ".core.rest");
        ConfigHolder.instance.setProperty("responseEnvelpe.path", ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath"));
        return true;
    }


}
