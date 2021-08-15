package cn.edu.scu.virjarjcd.feature.controller;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.virjarjcd.util.FieldMethodBuilder;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.IntrospectedTableUtil;
import cn.edu.scu.virjarjcd.util.StringUtils;

public class ControllerPlugin extends PluginAdapter {

	private String controllerPackage;
	private String sourcePath;


	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
			IntrospectedTable introspectedTable) {
		if(ConfigHolder.isSingleLayer()){
			return genSingleModelLayer(introspectedTable);
		}else{
			return genMultiModellayer(introspectedTable);
		}
	}

	private List<GeneratedJavaFile> genSingleModelLayer(IntrospectedTable introspectedTable){
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		String paramTypeStr = table.getDomainObjectName();
		FullyQualifiedJavaType paramType = new FullyQualifiedJavaType(paramTypeStr);
		String paramName = StringUtils.firstLetterLows(paramTypeStr);
		String serviceTypeStr = paramTypeStr+"Service";
		String bindingResultTypeStr = "BindingResult";
		FullyQualifiedJavaType BindingResultType = new FullyQualifiedJavaType(bindingResultTypeStr);

		Method javaBeansSetter = FieldMethodBuilder.getJavaBeansSetter(introspectedTable.getPrimaryKeyColumns().get(0), context, introspectedTable);
		String primaryKeySetter = javaBeansSetter.getName();


		String serviceName = StringUtils.firstLetterLows(serviceTypeStr);
		String requstUrl = ConfigHolder.instance.getProperty("sys.projectName", "virjar");
		String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
		FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);

		TopLevelClass topLevelClass = new TopLevelClass(controllerPackage+"."+paramTypeStr+"RestApiController");
		topLevelClass.addImportedType("org.slf4j.Logger");
		topLevelClass.addImportedType("org.slf4j.LoggerFactory");
		topLevelClass.addImportedType("org.springframework.http.ResponseEntity");
		topLevelClass.addImportedType("javax.annotation.Resource");
		topLevelClass.addImportedType("org.springframework.validation.BindingResult");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.PathVariable");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestBody");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
		topLevelClass.addImportedType("org.springframework.data.domain.Page");
		topLevelClass.addImportedType("org.springframework.data.domain.PageImpl");
		topLevelClass.addImportedType("org.springframework.data.domain.Pageable");
		topLevelClass.addImportedType("org.springframework.data.web.PageableDefault");
		topLevelClass.addImportedType("java.util.List");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.serviceApiPackage")+"."+paramTypeStr+"Service");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".model"+"."+paramTypeStr);
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("utils.package")+".ReturnUtil");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("responseEnvelpe.package")+".ResponseEnvelope");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("utils.package")+".ReturnCode");
		topLevelClass.addImportedType("org.springframework.stereotype.Controller");
		topLevelClass.addImportedType("javax.validation.Valid");

		topLevelClass.addAnnotation("@Controller");
		topLevelClass.addAnnotation("@RequestMapping(\"/" + requstUrl + "\")");

		topLevelClass.setVisibility(JavaVisibility.PUBLIC);

		String domainPathValue = paramTypeStr.toLowerCase();

		//field
		Field field = new Field();
		field.setName("logger");
		field.setType(new FullyQualifiedJavaType("Logger"));
		field.setFinal(true);
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setInitializationString("LoggerFactory.getLogger("+paramTypeStr+"RestApiController.class)");
		topLevelClass.addField(field);


		field = new Field();
		field.setName(serviceName);
		field.setType(new FullyQualifiedJavaType(serviceTypeStr));
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addAnnotation("@Resource");
		topLevelClass.addField(field);

		//method
		Method method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.GET)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("get"+paramTypeStr+"ById");
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		method.addBodyLine(paramTypeStr+" "+paramName +" = "+serviceName +".findByPrimaryKey(id);");
		method.addBodyLine("return ReturnUtil.retSuccess("+paramName+");");
		topLevelClass.addMethod(method);

		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"\", method = RequestMethod.POST)");
		method.setName("create"+paramTypeStr);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		Parameter parameter = new Parameter(paramType, paramName, "@RequestBody");
		parameter.addAnnotation("@Valid");
		method.addParameter(parameter);
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine("Integer  id = "+serviceName+".create("+paramName+");");
		method.addBodyLine("return ReturnUtil.retSuccess(id);");
		topLevelClass.addMethod(method);

		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.DELETE)");
		method.setName("delete"+paramTypeStr+"ByPrimaryKey");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		method.addBodyLine("Integer result = "+serviceName+".deleteByPrimaryKey(id);");
		method.addBodyLine("if(result == 1){");
		method.addBodyLine("return ReturnUtil.retSuccess(result);");
		method.addBodyLine("}else{");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.RECORD_NOT_EXIST,\"id=\"+id);}");
		topLevelClass.addMethod(method);

		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.PUT)");
		method.setName("update"+paramTypeStr+"ByPrimaryKeySelective");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		parameter = new Parameter(paramType, paramName, "@RequestBody");
		parameter.addAnnotation("@Valid");
		method.addParameter(parameter);
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine(paramName+"."+primaryKeySetter+"(id);");
		method.addBodyLine("Integer  result = "+serviceName+".updateByPrimaryKeySelective("+paramName+");");
		method.addBodyLine("if(result == 1){");
		method.addBodyLine("return ReturnUtil.retSuccess(id);");
		method.addBodyLine("}else{");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.RECORD_NOT_EXIST,\"id=\"+id);}");
		topLevelClass.addMethod(method);


		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/list\")");
		method.setName("list"+paramType+"s");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Pageable"), "pageable", "@PageableDefault"));
		method.addParameter(new Parameter(paramType, paramName));
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine("List<"+paramType+"> "+paramName+"s = "+serviceName+".selectPage("+paramName+",pageable);");
		method.addBodyLine("Page<"+paramType+"> page = new PageImpl<"+paramType+">("+paramName+"s,pageable,"+serviceName+".selectCount("+paramName+"));");
		method.addBodyLine("return ReturnUtil.retSuccess(page);");
		topLevelClass.addMethod(method);

		GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, sourcePath, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
		answer.add(javaFile);
		return answer;
	}

	//@Override
	private List<GeneratedJavaFile> genMultiModellayer(
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		String domainTypeStr = table.getDomainObjectName();
		String modelTypeStr = domainTypeStr+"Model";
		String serviceTypeStr = domainTypeStr+"Service";
		String voTypeStr = domainTypeStr+"VO";
		FullyQualifiedJavaType voType = new FullyQualifiedJavaType(voTypeStr);
		String bindingResultTypeStr = "BindingResult";
		FullyQualifiedJavaType BindingResultType = new FullyQualifiedJavaType(bindingResultTypeStr);


		Method javaBeansSetter = FieldMethodBuilder.getJavaBeansSetter(introspectedTable.getPrimaryKeyColumns().get(0), context, introspectedTable);
		String primaryKeySetter = javaBeansSetter.getName();

		String modelName = StringUtils.firstLetterLows(modelTypeStr);
		String serviceName = StringUtils.firstLetterLows(serviceTypeStr);
		String voName = StringUtils.firstLetterLows(voTypeStr);
		String requstUrl = ConfigHolder.instance.getProperty("sys.projectName", "virjar");
		String pkTypestr=IntrospectedTableUtil.getPKType(introspectedTable);
		FullyQualifiedJavaType pkType = new FullyQualifiedJavaType(pkTypestr);
		
		TopLevelClass topLevelClass = new TopLevelClass(controllerPackage+"."+domainTypeStr+"RestApiController");
		topLevelClass.addImportedType("org.slf4j.Logger");
		topLevelClass.addImportedType("org.slf4j.LoggerFactory");
		topLevelClass.addImportedType("org.springframework.http.ResponseEntity");
		topLevelClass.addImportedType("javax.annotation.Resource");
		topLevelClass.addImportedType("org.springframework.validation.BindingResult");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.PathVariable");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestBody");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
		topLevelClass.addImportedType("org.springframework.stereotype.Controller");
		topLevelClass.addImportedType("org.springframework.data.domain.Page");
		topLevelClass.addImportedType("org.springframework.data.domain.PageImpl");
		topLevelClass.addImportedType("org.springframework.data.domain.Pageable");
		topLevelClass.addImportedType("org.springframework.data.web.PageableDefault");
		topLevelClass.addImportedType("java.util.List");

		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("beanmappper.package")+".BeanMapper");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.serviceApiPackage")+"."+domainTypeStr+"Service");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".model"+"."+modelTypeStr);
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("sys.basePackage")+".vo"+"."+voTypeStr);
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("utils.package")+".ReturnUtil");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("responseEnvelpe.package")+".ResponseEnvelope");
		topLevelClass.addImportedType(ConfigHolder.instance.getProperty("utils.package")+".ReturnCode");
		//topLevelClass.addImportedType(ConfigHolder.instance.getProperty("responseEnvelpe.package")+".RestApiController");
		topLevelClass.addImportedType("javax.validation.Valid");

		topLevelClass.addAnnotation("@Controller");
		topLevelClass.addAnnotation("@RequestMapping(\"/"+requstUrl+"\")");
		
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		
		String domainPathValue = domainTypeStr.toLowerCase();
		
		//field
		Field field = new Field();
		field.setName("logger");
		field.setType(new FullyQualifiedJavaType("Logger"));
		field.setFinal(true);
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setInitializationString("LoggerFactory.getLogger("+domainTypeStr+"RestApiController.class)");
		topLevelClass.addField(field);
		
		field = new Field();
		field.setName("beanMapper");
		field.setType(new FullyQualifiedJavaType("BeanMapper"));
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addAnnotation("@Resource");
		topLevelClass.addField(field);
		
		field = new Field();
		field.setName(serviceName);
		field.setType(new FullyQualifiedJavaType(serviceTypeStr));
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addAnnotation("@Resource");
		topLevelClass.addField(field);
		
		//method
		Method method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.GET)");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("get"+domainTypeStr+"ById");
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		method.addBodyLine(modelTypeStr+" "+modelName +" = "+serviceName +".findByPrimaryKey(id);");
		method.addBodyLine(voTypeStr+" "+voName+" = beanMapper.map("+modelName+","+voTypeStr+".class);");
		method.addBodyLine("return ReturnUtil.retSuccess("+voName+");");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"\", method = RequestMethod.POST)");
		method.setName("create"+domainTypeStr);
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		Parameter parameter = new Parameter(voType, voName, "@RequestBody");
		parameter.addAnnotation("@Valid");
		method.addParameter(parameter);
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine(modelTypeStr+" "+modelName+" = beanMapper.map("+voName+","+modelTypeStr+".class);");
		method.addBodyLine("Integer  id = "+serviceName+".create("+modelName+");");
		method.addBodyLine("return ReturnUtil.retSuccess(id);");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.DELETE)");
		method.setName("delete"+domainTypeStr+"ByPrimaryKey");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		method.addBodyLine("Integer result = "+serviceName+".deleteByPrimaryKey(id);");
		method.addBodyLine("if(result == 1){");
		method.addBodyLine("return ReturnUtil.retSuccess(result);");
		method.addBodyLine("}else{");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.RECORD_NOT_EXIST,\"id=\"+id);}");
		topLevelClass.addMethod(method);
		
		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/{id}\", method = RequestMethod.PUT)");
		method.setName("update"+domainTypeStr+"ByPrimaryKeySelective");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(pkType, "id", "@PathVariable"));
		parameter = new Parameter(voType, voName, "@RequestBody");
		parameter.addAnnotation("@Valid");
		method.addParameter(parameter);
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine(modelTypeStr+" "+modelName+" = beanMapper.map("+voName+", "+modelTypeStr+".class);");
		method.addBodyLine(modelName+"."+primaryKeySetter+"(id);");
		method.addBodyLine("Integer  result = "+serviceName+".updateByPrimaryKeySelective("+modelName+");");
		method.addBodyLine("if(result == 1){");
		method.addBodyLine("return ReturnUtil.retSuccess(id);");
		method.addBodyLine("}else{");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.RECORD_NOT_EXIST,\"id=\"+id);}");
		topLevelClass.addMethod(method);

		method = new Method();
		method.addAnnotation("@RequestMapping(value = \"/"+domainPathValue+"/list\")");
		method.setName("list"+domainTypeStr+"s");
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(new FullyQualifiedJavaType("ResponseEntity<ResponseEnvelope<Object>>"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Pageable"), "pageable", "@PageableDefault"));
		method.addParameter(new Parameter(voType, voName));
		method.addParameter(new Parameter(BindingResultType, "bindingResult"));
		method.addBodyLine("if(bindingResult.hasErrors()){");
		method.addBodyLine("  String errorMessage =  bindingResult.getAllErrors().get(0).getDefaultMessage();");
		method.addBodyLine("return ReturnUtil.retException(ReturnCode.INPUT_PARAM_ERROR,errorMessage);");
		method.addBodyLine("}");
		method.addBodyLine("List<"+modelTypeStr+"> "+modelName+"s = "+serviceName+".selectPage(beanMapper.map("+voName+", "+modelTypeStr+".class),pageable);");
		method.addBodyLine("Page<"+voType+"> page = new PageImpl<"+voType+">(beanMapper.mapAsList("+modelName+"s,"+voType+".class),pageable,"+serviceName+".selectCount(beanMapper.map("+voName+","+modelTypeStr+".class)));");
		method.addBodyLine("return ReturnUtil.retSuccess(page);");
		topLevelClass.addMethod(method);
		GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, sourcePath, context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
		answer.add(javaFile);
		return answer;
	}


	@Override
	public boolean validate(List<String> warnings) {
		controllerPackage = ConfigHolder.instance.getProperty("sys.basePackage", "com.virjar")+".controller";
		ConfigHolder.instance.setProperty("sys.controllerPackage", controllerPackage);

		sourcePath = ConfigHolder.instance.getProperty("sys.webapp.javasourcepath");

		return true;
	}
}
