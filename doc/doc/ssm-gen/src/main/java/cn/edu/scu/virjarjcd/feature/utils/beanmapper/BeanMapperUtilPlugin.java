package cn.edu.scu.virjarjcd.feature.utils.beanmapper;

import cn.edu.scu.virjarjcd.codegertator.IVirjarComponent;
import cn.edu.scu.virjarjcd.dom.java.VirjarAnnotation;
import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;
import cn.edu.scu.virjarjcd.util.LombokErasure;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeanMapperUtilPlugin extends MergePluginAdpter{

    private boolean hasvalidate = false;


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        // TODO Auto-generated method stub
        //return super.contextGenerateAdditionalJavaFiles();

        String classPackage = ConfigHolder.instance.getProperty("beanmappper.package", "");
        String path = ConfigHolder.instance.getProperty("beanmappper.path", "");
        FileUtils.createIfNotExist(path);
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        genClassTemplate(classPackage, path, "MapClassScanner", answer);
        genClassTemplate(classPackage, path, "OrikaBeanMapper", answer);

        genInterfaceTemplate(classPackage, path, "BeanMapper", answer);

        genAnnotationTemplate(classPackage, path, "MapClass", answer);
        genAnnotationTemplate(classPackage, path, "MapField", answer);
        return answer;
    }

    private void genInterfaceTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            Interface interfaze = new JDTCodeParser().parseInterface(BeanMapperUtilPlugin.class.getResourceAsStream(fileName), classPackage);
            GeneratedJavaFile gjf = new GeneratedJavaFile(interfaze, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void genAnnotationTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            VirjarAnnotation parseAnotation = new JDTCodeParser().parseAnotation(BeanMapperUtilPlugin.class.getResourceAsStream(fileName), classPackage);
            GeneratedJavaFile gjf = new GeneratedJavaFile(parseAnotation, path, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            answer.add(gjf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void genClassTemplate(String classPackage, String path, String fileName, List<GeneratedJavaFile> answer) {
        try {
            TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(BeanMapperUtilPlugin.class.getResourceAsStream(fileName), classPackage);
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


    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        ConfigHolder.instance.setProperty("beanmappper.package", ConfigHolder.instance.getProperty("sys.basePackage") + ".core.beanmapper");
        ConfigHolder.instance.setProperty("beanmappper.path", ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath"));
        hasvalidate = true;
        return !ConfigHolder.isSingleLayer();
    }

}
