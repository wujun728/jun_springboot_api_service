package cn.edu.scu.virjarjcd.feature.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.IntrospectedTableUtil;
import cn.edu.scu.virjarjcd.util.StringUtils;

public class ServiceApiPlugin extends PluginAdapter {

	private String serviceApiPackage;
	private String serviceApiImplPackage;
	private String servicePath;
	private String serviceimpilPath;
	
	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		serviceApiPackage = ConfigHolder.instance.getProperty("sys.basePackage","com.virjar")+".service";
		ConfigHolder.instance.setProperty("sys.serviceApiPackage", serviceApiPackage);
		serviceimpilPath = ConfigHolder.instance.getProperty("sys.service.javasourcepath");
		servicePath = ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath");
		
		serviceApiImplPackage = serviceApiPackage+".impl";
		ConfigHolder.instance.setProperty("sys.serviceApiImpilPackage", serviceApiImplPackage);
		return true;
	}

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        answer.addAll(genServiceApiJavaFiles(introspectedTable));
        if(ConfigHolder.isSingleLayer()){
            answer.addAll(genServiceJavaFile4Single(introspectedTable));
        }else{
            answer.addAll(genServiceJavaFiles4Multi(introspectedTable));
        }
        return answer;
    }

    private List<GeneratedJavaFile> genServiceJavaFile4Single(IntrospectedTable introspectedTable){
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        String repositoryTypeStr = table.getDomainObjectName()+"Repository";
        FullyQualifiedJavaType RepositoryType = new FullyQualifiedJavaType(repositoryTypeStr);
        String repositoryName = StringUtils.firstLetterLows(table.getDomainObjectName()+"Repo");


        String paramTypestr = table.getDomainObjectName();
        String paramName = StringUtils.firstLetterLows(paramTypestr);
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(paramTypestr);

        String modelpath = ConfigHolder.instance.get("sys.basePackage")+".model."+paramTypestr;
        String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
        FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);


        TopLevelClass topLevelClass = new TopLevelClass(serviceApiImplPackage+"."+table.getDomainObjectName()+"ServiceImpl");
        topLevelClass.addImportedType("javax.annotation.Resource");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("org.springframework.transaction.annotation.Transactional");
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.entitypackage")+"."+table.getDomainObjectName());
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.mapperinterfacepackage")+"."+table.getDomainObjectName()+"Repository");
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".model."+table.getDomainObjectName());
        topLevelClass.addImportedType(serviceApiPackage+"."+table.getDomainObjectName()+"Service");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("org.springframework.data.domain.Pageable");

        topLevelClass.addAnnotation("@Service");
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType(serviceApiPackage+"."+table.getDomainObjectName()+"Service"));

        Field field = new Field();
        field.addAnnotation("@Resource");
        field.setType(RepositoryType);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setName(repositoryName);
        topLevelClass.addField(field);

        Method method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("create");
        method.addParameter(new Parameter(paramType, paramName));
        String codeline = "return "+repositoryName+".insert("+paramName+");";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("createSelective");
        method.addParameter(new Parameter(paramType, paramName));
        codeline = "return "+repositoryName+".insertSelective("+paramName+");";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("deleteByPrimaryKey");
        method.addParameter(new Parameter(pkType, "id"));
        codeline = "return "+repositoryName+".deleteByPrimaryKey(id);";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(paramType);
        method.setName("findByPrimaryKey");
        method.addParameter(new Parameter(pkType, "id"));
        method.addBodyLine("return "+repositoryName+".selectByPrimaryKey(id);");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("selectCount");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".selectCount("+paramName+");");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("updateByPrimaryKey");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".updateByPrimaryKey("+paramName+");");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("updateByPrimaryKeySelective");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".updateByPrimaryKeySelective("+paramName+");");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<"+paramType+">"));
        method.setName("selectPage");
        method.addParameter(new Parameter(paramType, paramName));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Pageable"), "pageable"));
        method.addBodyLine("return "+repositoryName+".selectPage("+paramName+",pageable);");
        topLevelClass.addMethod(method);

        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(topLevelClass,serviceimpilPath,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
        answer.add(generatedJavaFile);
        return answer;
    }

    private List<GeneratedJavaFile> genServiceJavaFiles4Multi(IntrospectedTable introspectedTable){
        //service impl
        //return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        String repositoryTypeStr = table.getDomainObjectName()+"Repository";
        FullyQualifiedJavaType RepositoryType = new FullyQualifiedJavaType(repositoryTypeStr);
        String repositoryName = StringUtils.firstLetterLows(table.getDomainObjectName()+"Repo");

        String modelTypeStr = table.getDomainObjectName()+"Model";
        FullyQualifiedJavaType ModelType = new FullyQualifiedJavaType(modelTypeStr);
        String modelName = StringUtils.firstLetterLows(modelTypeStr);

        String entityTypeStr = table.getDomainObjectName();
        String entityName = StringUtils.firstLetterLows(entityTypeStr);

        String paramTypestr = table.getDomainObjectName()+"Model";
        String paramName = StringUtils.firstLetterLows(paramTypestr);
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(paramTypestr);

        String modelpath = ConfigHolder.instance.get("sys.basePackage")+".model."+paramTypestr;
        String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
        FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);


        TopLevelClass topLevelClass = new TopLevelClass(serviceApiImplPackage+"."+table.getDomainObjectName()+"ServiceImpl");
        topLevelClass.addImportedType("javax.annotation.Resource");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("org.springframework.transaction.annotation.Transactional");
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.entitypackage")+"."+table.getDomainObjectName());
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.mapperinterfacepackage")+"."+table.getDomainObjectName()+"Repository");
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".model."+table.getDomainObjectName()+"Model");
        topLevelClass.addImportedType(serviceApiPackage+"."+table.getDomainObjectName()+"Service");
        topLevelClass.addImportedType(ConfigHolder.instance.getProperty("beanmappper.package")+".BeanMapper");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("org.springframework.data.domain.Pageable");

        topLevelClass.addAnnotation("@Service");
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType(serviceApiPackage+"."+table.getDomainObjectName()+"Service"));

        Field field = new Field();
        field.addAnnotation("@Resource");
        field.setName("beanMapper");
        field.setType(new FullyQualifiedJavaType("BeanMapper"));
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);

        field = new Field();
        field.addAnnotation("@Resource");
        field.setType(RepositoryType);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setName(repositoryName);
        topLevelClass.addField(field);

        Method method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("create");
        method.addParameter(new Parameter(paramType, paramName));
        String codeline = "return "+repositoryName+".insert(beanMapper.map("+modelName+","+entityTypeStr+".class));";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("createSelective");
        method.addParameter(new Parameter(paramType, paramName));
        codeline = "return "+repositoryName+".insertSelective(beanMapper.map("+modelName+","+entityTypeStr+".class));";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("deleteByPrimaryKey");
        method.addParameter(new Parameter(pkType, "id"));
        codeline = "return "+repositoryName+".deleteByPrimaryKey(id);";
        method.addBodyLine(codeline);
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(ModelType);
        method.setName("findByPrimaryKey");
        method.addParameter(new Parameter(pkType, "id"));
        method.addBodyLine(entityTypeStr +" "+entityName +" = "+repositoryName+".selectByPrimaryKey(id);");
        method.addBodyLine("return beanMapper.map("+entityName+","+modelTypeStr+".class);");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("selectCount");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".selectCount(beanMapper.map("+modelName+","+entityTypeStr+".class));");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("updateByPrimaryKey");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".updateByPrimaryKey(beanMapper.map("+modelName+","+entityTypeStr+".class));");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setName("updateByPrimaryKeySelective");
        method.addParameter(new Parameter(paramType, paramName));
        method.addBodyLine("return "+repositoryName+".updateByPrimaryKeySelective(beanMapper.map("+modelName+","+entityTypeStr+".class));");
        topLevelClass.addMethod(method);

        method = new Method();
        method.addAnnotation("@Transactional(readOnly = true)");
        method.addAnnotation("@Override");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<"+paramType+">"));
        method.setName("selectPage");
        method.addParameter(new Parameter(paramType, paramName));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Pageable"), "pageable"));
        method.addBodyLine("List<"+entityTypeStr +"> "+entityName +"List = "+repositoryName+".selectPage(beanMapper.map("+paramName+","+entityTypeStr+".class),pageable);");
        method.addBodyLine("return beanMapper.mapAsList("+entityName+"List,"+modelTypeStr+".class);");

        topLevelClass.addMethod(method);

        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(topLevelClass,serviceimpilPath,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
        answer.add(generatedJavaFile);
        return answer;
    }

    private List<GeneratedJavaFile> genServiceApiJavaFiles(IntrospectedTable introspectedTable){
        //return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        Interface interfac = new Interface(serviceApiPackage+"."+table.getDomainObjectName()+"Service");

        String paramTypestr ;
        if(ConfigHolder.isSingleLayer()){
            paramTypestr = table.getDomainObjectName();
        } else{
            paramTypestr = table.getDomainObjectName()+"Model";

        }
        String paramName = StringUtils.firstLetterLows(paramTypestr);
        FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(paramTypestr);

        Set<String> imports = IntrospectedTableUtil.tableRelateImportCalc(introspectedTable);
        for(String impor:imports){
            interfac.addImportedType(new FullyQualifiedJavaType(impor));
        }
        //import model ,暂时硬编码
        String modelpath = ConfigHolder.instance.get("sys.basePackage")+".model."+paramTypestr;
        String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
        FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);

        interfac.addImportedType(new FullyQualifiedJavaType(modelpath));
        interfac.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfac.addImportedType(new FullyQualifiedJavaType("org.springframework.data.domain.Pageable"));

        interfac.setVisibility(JavaVisibility.PUBLIC);

        Method method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("create");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(paramType, paramName));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("createSelective");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(paramType, paramName));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("findByPrimaryKey");
        method.setReturnType(new FullyQualifiedJavaType(paramTypestr));
        method.addParameter(new Parameter(pkType, "id"));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByPrimaryKey");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(paramType, paramName));


        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByPrimaryKeySelective");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(paramType, paramName));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("deleteByPrimaryKey");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(pkType, "id"));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("selectCount");
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.addParameter(new Parameter(paramType, paramName));

        method = new Method();
        interfac.addMethod(method);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("selectPage");
        method.setReturnType(new FullyQualifiedJavaType("List<"+paramTypestr+">"));
        method.addParameter(new Parameter(paramType, paramName));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Pageable"), "Pageable"));


        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(interfac,servicePath,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
        answer.add(generatedJavaFile);
        return answer;
    }

	//@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles1(
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		//return super.contextGenerateAdditionalJavaFiles(introspectedTable);
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		Interface interfac = new Interface(serviceApiPackage+"."+table.getDomainObjectName()+"Service");

		String paramTypestr = table.getDomainObjectName()+"Model";
		String paramName = StringUtils.firstLetterLows(paramTypestr);
		FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(paramTypestr);
		
		Set<String> imports = IntrospectedTableUtil.tableRelateImportCalc(introspectedTable);
		for(String impor:imports){
			interfac.addImportedType(new FullyQualifiedJavaType(impor));
		}
		//import model ,暂时硬编码
		String modelpath = ConfigHolder.instance.get("sys.basePackage")+".model."+paramTypestr;
		String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
		FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);
		
		interfac.addImportedType(new FullyQualifiedJavaType(modelpath));
		
		interfac.setVisibility(JavaVisibility.PUBLIC);
		
		Method method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("create");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(paramType, paramName));
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("createSelective");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(paramType, paramName));
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("findByPrimaryKey");
		method.setReturnType(new FullyQualifiedJavaType(paramTypestr));
		method.addParameter(new Parameter(pkType, "id"));
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("updateByPrimaryKey");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(paramType, paramName));	
		
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("updateByPrimaryKeySelective");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(paramType, paramName));
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("deleteByPrimaryKey");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(pkType, "id"));
		
		method = new Method();
		interfac.addMethod(method);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("selectCount");
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.addParameter(new Parameter(paramType, paramName));
		
		GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(interfac,servicePath,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
		answer.add(generatedJavaFile);
		
		
		
		//service impl 
		String repositoryTypeStr = table.getDomainObjectName()+"Repository";
		FullyQualifiedJavaType RepositoryType = new FullyQualifiedJavaType(repositoryTypeStr);
		String repositoryName = StringUtils.firstLetterLows(table.getDomainObjectName()+"Repo");
		
		String modelTypeStr = table.getDomainObjectName()+"Model";
		FullyQualifiedJavaType ModelType = new FullyQualifiedJavaType(modelTypeStr);
		String modelName = StringUtils.firstLetterLows(modelTypeStr);
		
		String entityTypeStr = table.getDomainObjectName();
		String entityName = StringUtils.firstLetterLows(entityTypeStr);
		
		TopLevelClass topLevelClass = new TopLevelClass(serviceApiImplPackage+"."+table.getDomainObjectName()+"ServiceImpl");
		topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
		topLevelClass.addImportedType("org.springframework.stereotype.Service");
		topLevelClass.addImportedType("org.springframework.transaction.annotation.Transactional");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.entitypackage")+"."+table.getDomainObjectName());
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.mapperinterfacepackage")+"."+table.getDomainObjectName()+"Repository");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".model."+table.getDomainObjectName()+"Model");
		topLevelClass.addImportedType(serviceApiPackage+"."+table.getDomainObjectName()+"Service");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("beanmappper.package")+".BeanMapper");
		
		topLevelClass.addAnnotation("@Service");
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(serviceApiPackage+"."+table.getDomainObjectName()+"Service"));
		
		Field field = new Field();
		field.addAnnotation("@Resource");
		field.setName("beanMapper");
		field.setType(new FullyQualifiedJavaType("BeanMapper"));
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
		
		field = new Field();
		field.addAnnotation("@Resource");
		field.setType(RepositoryType);
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setName(repositoryName);
		topLevelClass.addField(field);
		
		method = new Method();
		method.addAnnotation("@Transactional");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("create");
		method.addParameter(new Parameter(paramType, paramName));
		String codeline = "return "+repositoryName+".insert(beanMapper.map("+modelName+","+entityTypeStr+".class));";
		method.addBodyLine(codeline);
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("createSelective");
		method.addParameter(new Parameter(paramType, paramName));
		codeline = "return "+repositoryName+".insertSelective(beanMapper.map("+modelName+","+entityTypeStr+".class));";
		method.addBodyLine(codeline);
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("deleteByPrimaryKey");
		method.addParameter(new Parameter(pkType, "id"));
		codeline = "return "+repositoryName+".deleteByPrimaryKey(id);";
		method.addBodyLine(codeline);
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional(readOnly = true)");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(ModelType);
		method.setName("findByPrimaryKey");
		method.addParameter(new Parameter(pkType, "id"));
		method.addBodyLine(entityTypeStr +" "+entityName +" = "+repositoryName+".selectByPrimaryKey(id);");
		method.addBodyLine("return beanMapper.map("+entityName+","+modelTypeStr+".class);");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional(readOnly = true)");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("selectCount");
		method.addParameter(new Parameter(paramType, paramName));
		method.addBodyLine("return "+repositoryName+".selectCount(beanMapper.map("+modelName+","+entityTypeStr+".class));");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("updateByPrimaryKey");
		method.addParameter(new Parameter(paramType, paramName));
		method.addBodyLine("return "+repositoryName+".updateByPrimaryKey(beanMapper.map("+modelName+","+entityTypeStr+".class));");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@Transactional");
		method.addAnnotation("@Override");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("int"));
		method.setName("updateByPrimaryKeySelective");
		method.addParameter(new Parameter(paramType, paramName));
		method.addBodyLine("return "+repositoryName+".updateByPrimaryKeySelective(beanMapper.map("+modelName+","+entityTypeStr+".class));");
		topLevelClass.addMethod(method);
		
		generatedJavaFile = new GeneratedJavaFile(topLevelClass,serviceimpilPath,context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),context.getJavaFormatter());
		answer.add(generatedJavaFile);
		return answer;
	}

	
}
