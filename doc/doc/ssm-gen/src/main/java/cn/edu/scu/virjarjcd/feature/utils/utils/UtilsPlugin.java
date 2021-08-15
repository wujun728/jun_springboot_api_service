package cn.edu.scu.virjarjcd.feature.utils.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.TopLevelEnumeration;
import org.mybatis.generator.config.PropertyRegistry;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;

public class UtilsPlugin extends MergePluginAdpter {

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		// TODO Auto-generated method stub
		//return super.contextGenerateAdditionalJavaFiles();

		String classPackage = ConfigHolder.instance.getProperty("utils.package","");
		String path = ConfigHolder.instance.getProperty("utils.path", "");
		FileUtils.createIfNotExist(path);
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

		genClassTemplate(classPackage, path, "ReturnInfo", answer);
		genReturnUtil(classPackage, path, "ReturnUtil", answer);

		genEnumeTemplate(classPackage, path, "ReturnCode", answer);
		return answer;
	}

	private void genReturnUtil(String classPackage,String path,String fileName,List<GeneratedJavaFile> answer){
		try {
			TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(UtilsPlugin.class.getResourceAsStream(fileName),classPackage);
			mapClassScanner.addImportedType(ConfigHolder.instance.getProperty("responseEnvelpe.package")+".ResponseEnvelope");
			mapClassScanner.addImportedType(ConfigHolder.instance.getProperty("core.exception.package")+".RestApiError");
			mapClassScanner.addImportedType(ConfigHolder.instance.getProperty("core.exception.package")+".BusinessException");
			GeneratedJavaFile gjf = new GeneratedJavaFile(mapClassScanner,path,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
			answer.add(gjf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void genClassTemplate(String classPackage,String path,String fileName,List<GeneratedJavaFile> answer){
		try {
			TopLevelClass mapClassScanner = new JDTCodeParser().parseClass(UtilsPlugin.class.getResourceAsStream(fileName),classPackage);
			GeneratedJavaFile gjf = new GeneratedJavaFile(mapClassScanner,path,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
			answer.add(gjf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void genEnumeTemplate(String classPackage,String path,String fileName,List<GeneratedJavaFile> answer){
		try {
			TopLevelEnumeration parseEnum = new JDTCodeParser().parseEnum(UtilsPlugin.class.getResourceAsStream(fileName),classPackage);
			GeneratedJavaFile gjf = new GeneratedJavaFile(parseEnum,path,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
			answer.add(gjf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		ConfigHolder.instance.setProperty("utils.package", ConfigHolder.instance.getProperty("sys.basePackage")+".core.utils");
		ConfigHolder.instance.setProperty("utils.path",ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath"));
		return true;
	}


}
