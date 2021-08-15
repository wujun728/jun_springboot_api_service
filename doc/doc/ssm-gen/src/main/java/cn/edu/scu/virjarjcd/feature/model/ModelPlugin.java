package cn.edu.scu.virjarjcd.feature.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import cn.edu.scu.virjarjcd.exception.CodegenException;
import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FieldMethodBuilder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;

public class ModelPlugin extends MergePluginAdpter {

	private String javaSourcePath ;
	private String modelPackage;


	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		modelPackage = ConfigHolder.instance.getProperty("sys.basePackage")+".model";
		ConfigHolder.instance.setProperty("sys.modelPackage", modelPackage);

		javaSourcePath = ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath");
		return ConfigHolder.isIsmerge();
	}

	@Override
	public List<GeneratedJavaFile> mergeAdditionalJavaFiles(
			IntrospectedTable introspectedTable, String[] args) {
		// TODO Auto-generated method stub
		List<GeneratedJavaFile> answer = new ArrayList<>();
		String modeAndField = args[3];
		String fieldName = modeAndField.split("\\.")[1];
		String domainName = introspectedTable.getFullyQualifiedTable().getDomainObjectName()+"Model";
		String javaFilePath = FileUtils.getJavaFilePath(javaSourcePath, modelPackage, domainName);
		try {
			TopLevelClass levelClass = new JDTCodeParser().parseClass(new FileInputStream(javaFilePath), modelPackage);

			//检查原数据模型中是否存在需要添加的字段
			for(Field field:levelClass.getFields()){
				if(field.getName().toLowerCase().equals(fieldName.toLowerCase())){
					throw new CodegenException("field "+fieldName+" already defined in mode "+domainName);
				}
			}

			IntrospectedColumn addedColumn = null;
			for(IntrospectedColumn introspectedColumn:introspectedTable.getAllColumns()){
				if(introspectedColumn.getFullyQualifiedJavaType().getShortName().toLowerCase().equals(fieldName)){
					addedColumn = introspectedColumn;
					break;
				}
			}
			if(addedColumn == null){
				throw new CodegenException("can not find filed "
						+fieldName +" in table "
						+introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()
						+" Please update db before run this command");
			}

			//增加field
			Field javaBeansField = FieldMethodBuilder.getJavaBeansField(addedColumn, context, introspectedTable);
			levelClass.addField(javaBeansField);

			//lombok特性判断
			if(ConfigHolder.isLombok()){
				String lombokAnnotations = ConfigHolder.instance.getProperty("lombok.annotations","Data");
				if(lombokAnnotations.toLowerCase().contains("getter")){
					javaBeansField.addAnnotation("@Getter");
				}else if(lombokAnnotations.toLowerCase().contains("getter")){
					javaBeansField.addAnnotation("@Setter");
				}
			}else{//构造getter setter方法
				Method method = FieldMethodBuilder.getJavaBeansGetter(addedColumn, context, introspectedTable);
				levelClass.addMethod(method);
				if(!introspectedTable.isImmutable()){
					method = FieldMethodBuilder.getJavaBeansSetter(addedColumn, context, introspectedTable);
					levelClass.addMethod(method);
				}
			}


			GeneratedJavaFile gjf = new GeneratedJavaFile(levelClass, javaFilePath, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
					context.getJavaFormatter());
			answer.add(gjf);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}


}
