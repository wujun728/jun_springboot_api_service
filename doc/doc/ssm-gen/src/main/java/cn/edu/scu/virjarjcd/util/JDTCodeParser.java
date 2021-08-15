package cn.edu.scu.virjarjcd.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.TopLevelEnumeration;

import cn.edu.scu.virjarjcd.dom.java.ParameterTopLevelClass;
import cn.edu.scu.virjarjcd.dom.java.VirjarAnnotation;
import cn.edu.scu.virjarjcd.dom.java.VirjarFullyQualifiedJavaType;
import cn.edu.scu.virjarjcd.dom.java.VirjarMethod;
import cn.edu.scu.virjarjcd.exception.CodegenException;
import cn.edu.scu.virjarjcd.exception.SourceFileNotSupportException;

/** 
 * 使用eclipse的jdt来对java源代码进行解析,<br/>
 * 注意，一个文件中只允许有一个类，多余两个的只会解析第一个
 */
public class JDTCodeParser {
	//private InputStream is;
	private ASTParser astParser = ASTParser.newParser(AST.JLS8); // 非常慢
	//mybatis codegen的docline，包含注释（不需要去掉注释标志），但是需要清楚行首空白
	private Pattern doclinePattern = Pattern.compile("^\\s*(.*)$",Pattern.MULTILINE);
	protected DefaultCodeFormatter defaultCodeFormatter;
	//private Pattern packagePattern = Pattern.compile("package\\s+(.+);");

	public JDTCodeParser(){
		Hashtable options = JavaCore.getOptions();
		options.put("org.eclipse.jdt.core.compiler.source", "1.8");
		options.put("org.eclipse.jdt.core.compiler.codegen.targetPlatform", "1.8");
		options.put("org.eclipse.jdt.core.compiler.compliance", "1.8");
		astParser.setCompilerOptions(options);

		//重置eclipse选项，加载默认格式化规则(conf/eclipse_format_setting.xml)
		Properties properties = FileUtils.readXmlJavaSettingsFile(new File("conf/eclipse_format_setting.xml"), new Properties(), "virjar");
		trimTrailingWhitespaceFromConfigValues(properties);
		properties.setProperty("org.eclipse.jdt.core.compiler.source", "1.8");
		properties.setProperty("org.eclipse.jdt.core.compiler.codegen.targetPlatform", "1.8");
		properties.setProperty("org.eclipse.jdt.core.compiler.compliance", "1.8");
		defaultCodeFormatter = new DefaultCodeFormatter(properties);
	}
	protected void trimTrailingWhitespaceFromConfigValues(Properties config) {
		// First trim the values and store the trimmed values in a temporary map.
		Map<String, String> map = new HashMap<String, String>(config.size());
		for (Object key : config.keySet()) {
			String optionName = (String) key;
			String optionValue = config.getProperty(optionName);
			map.put(optionName, (optionValue != null) ? optionValue.trim() : null);
		}
		// Then copy the values back to the original Properties object.
		for (String key : map.keySet()) {
			config.setProperty(key, map.get(key));
		}
	}
	public String format(String source) throws MalformedTreeException, BadLocationException{
		//CompilationUnit compilationUnit = JDTParse(source);
		//		System.out.println("----------------before----------------------");
		//		System.out.println(source);
		//		System.out.println("-------------------after-------------------");
		//		compilationUnit.recordModifications();
		//		Document doc = new Document();
		//		TextEdit edits = compilationUnit.rewrite(doc,null);
		//		edits.apply(doc);
		//		System.out.println(doc.get());
		//		return doc.get();
		//return compilationUnit.toString();
		//import优化

		IDocument doc = new Document();
		try {
			doc.set(source);
			TextEdit edit = defaultCodeFormatter.format(
					CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS, source, 0,
					source.length(), 0,"\n");
			if (edit != null) {
				edit.apply(doc);
			} else {
				throw new CodegenException("source file can not formatted:\n"+source);
			}
			return doc.get();
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}

	}

	public TopLevelEnumeration parseEnum(InputStream is,String clazzPackage) throws IOException{
		//this.is = is;
		CompilationUnit compilationUnit = JDTParse(is);

		String typeName ="";
		List types = compilationUnit.types();
		EnumDeclaration clazz;
		if(types != null && types.size() >0){
			clazz = (EnumDeclaration) types.get(0);
			typeName = clazz.getName().getFullyQualifiedName();
		}else{
			return null;
		}

		TopLevelEnumeration topLevelEnumeration = 
				new TopLevelEnumeration(new FullyQualifiedJavaType(clazzPackage+"."+typeName));
		//import处理
		addImports(topLevelEnumeration,compilationUnit);

		//class注释处理
		Javadoc javadoc = clazz.getJavadoc();
		if(javadoc != null){
			List<String> docLines = parseDocLine(javadoc.toString());
			for(String comment:docLines){
				topLevelEnumeration.addJavaDocLine(comment);
			}
		}
		
		//注解处理
		List modifiers = clazz.modifiers();
		for(Object obj: modifiers){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				topLevelEnumeration.addAnnotation(annotation.toString());
			}else if(obj instanceof Modifier){
				processEnumeModifier(topLevelEnumeration,(Modifier)obj);
			}
		}
		List bodyDeclarations = clazz.bodyDeclarations();
		for(Object obj:bodyDeclarations){
			if(obj instanceof FieldDeclaration){
				parseEnumField((FieldDeclaration)obj, topLevelEnumeration);
			}else if(obj instanceof MethodDeclaration){
				parseMethod((MethodDeclaration)obj,topLevelEnumeration);
			}else{
				throw new CodegenException("not support convert type:"+obj.getClass());
			}
		}


		//枚举常亮处理
		List<EnumConstantDeclaration> enumConstants = clazz.enumConstants();
		for(EnumConstantDeclaration enumConstantDeclaration:enumConstants){
			//System.out.println(enumConstantDeclaration.getName().getFullyQualifiedName());
			topLevelEnumeration.addEnumConstant(enumConstantDeclaration.toString());
		}

		return topLevelEnumeration;
	}

	private void processEnumeModifier(TopLevelEnumeration topLevelEnumeration,
			Modifier modifier) {
		// TODO Auto-generated method stub
		if(modifier.isFinal()){
			topLevelEnumeration.setFinal(true);
		}else if(modifier.isNative()){
			throw new SourceFileNotSupportException("not support native modifier");
		}else if(modifier.isPrivate()){
			topLevelEnumeration.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			topLevelEnumeration.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			topLevelEnumeration.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			topLevelEnumeration.setStatic(true);
		}else if(modifier.isSynchronized()){
			throw new SourceFileNotSupportException("not support synchroized modifier");
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			throw new SourceFileNotSupportException("not support abstract modifier");
		}
	}

	private void parseEnumField(FieldDeclaration fieldDeclaration,TopLevelEnumeration topLevelEnumeration){
		Field field = new Field();
		topLevelEnumeration.addField(field);
		field.setType(new FullyQualifiedJavaType(fieldDeclaration.getType().toString()));
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
		field.setName(fragment.getName().getFullyQualifiedName());
		if(fragment.getInitializer() != null)
			field.setInitializationString(fragment.getInitializer().toString());
		List fieldModifiers = fieldDeclaration.modifiers();
		for(Object obj:fieldModifiers){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				field.addAnnotation(annotation.toString());
			}else if(obj instanceof Modifier){
				processFieldModifier(field,(Modifier)obj);
			}
		}
	}

	private void parseMethod(MethodDeclaration method,
			TopLevelEnumeration topLevelEnumeration) {
		// TODO Auto-generated method stub

		Method mybatisMethod = null;
		//泛型方法
		List<TypeParameter> typeParameters = method.typeParameters();
		if(typeParameters.size() >0){
			mybatisMethod = new VirjarMethod();
			VirjarMethod virjarMethod = (VirjarMethod) mybatisMethod;
			for(TypeParameter parameter:typeParameters){
				virjarMethod.addTypeParameter(parameter.toString());
			}
		}else{
			mybatisMethod = new Method();
		}

		topLevelEnumeration.addMethod(mybatisMethod);

		//注释处理
		Javadoc javadoc = method.getJavadoc();
		if(javadoc !=null){
			List<String> docLines = parseDocLine(method.getJavadoc().toString());
			for(String line:docLines){
				mybatisMethod.addJavaDocLine(line);
			}
		}

		//注解处理,modifier处理
		for(Object obj : method.modifiers()){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				mybatisMethod.addAnnotation(annotation.toString());
			}else if(obj instanceof Modifier){
				processMethodModifier(mybatisMethod,(Modifier)obj);
			}
		}

		mybatisMethod.setName(method.getName().getFullyQualifiedName());
		if(method.isConstructor()){
			mybatisMethod.setConstructor(true);
		}else{
			Type returnType = method.getReturnType2();
			if(returnType != null){
				mybatisMethod.setReturnType(new FullyQualifiedJavaType(returnType.toString()));
			}
		}
		//参数列表
		List parameters = method.parameters();
		for(Object obj:parameters){
			SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration)obj;
			Parameter parameter = new Parameter(new VirjarFullyQualifiedJavaType(singleVariableDeclaration.getType().toString()), singleVariableDeclaration.getName().getFullyQualifiedName(), singleVariableDeclaration.isVarargs());
			mybatisMethod.addParameter(parameter);
			List modifiers = singleVariableDeclaration.modifiers();
			for(Object anotationObj:modifiers){
				if(anotationObj instanceof Annotation){
					Annotation annotation = (Annotation)anotationObj;
					parameter.addAnnotation(annotation.toString());
				}
			}
		}

		processBodyLine(method.getBody().toString(), mybatisMethod);

	}

	private void addImports(TopLevelEnumeration topLevelEnumeration,
			CompilationUnit compilationUnit) {
		// TODO Auto-generated method stub
		List<ImportDeclaration> imports = compilationUnit.imports();
		for(ImportDeclaration importDeclaration : imports){
			topLevelEnumeration.addImportedType(new FullyQualifiedJavaType(importDeclaration.getName().getFullyQualifiedName()));
		}
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public VirjarAnnotation parseAnotation(InputStream is,String clazzPackage) throws IOException{
		//this.is = is;
		CompilationUnit compilationUnit = JDTParse(is);

		String typeName ="";
		List types = compilationUnit.types();
		AnnotationTypeDeclaration annotaion;
		if(types != null && types.size() >0){
			annotaion = (AnnotationTypeDeclaration) types.get(0);
			typeName = annotaion.getName().getFullyQualifiedName();
		}else{
			return null;
		}

		VirjarAnnotation answer = new VirjarAnnotation(clazzPackage+"."+typeName);
		//import处理
		addImports(answer,compilationUnit);
		//class注释处理
		Javadoc javadoc = annotaion.getJavadoc();
		if(javadoc != null){
			List<String> docLines = parseDocLine(javadoc.toString());
			for(String comment:docLines){
				answer.addJavaDocLine(comment);
			}
		}

		//注解处理
		List modifiers = annotaion.modifiers();
		for(Object obj: modifiers){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				answer.addAnnotation(obj.toString());
			}else if(obj instanceof Modifier){
				processAnotationModifier(answer,(Modifier)obj);
			}
		}

		parseAnnotaionMethod(annotaion.bodyDeclarations(), answer);
		return answer;
	}

	private void parseAnnotaionMethod(List annotationTypeMemberDeclarations,VirjarAnnotation answer){
		for(Object obj:annotationTypeMemberDeclarations){
			VirjarAnnotation.Method method = new VirjarAnnotation.Method();
			answer.addMethod(method);

			Javadoc javadoc = null;

			if(obj instanceof AnnotationTypeMemberDeclaration){
				AnnotationTypeMemberDeclaration annotationMember = (AnnotationTypeMemberDeclaration) obj;
				//javadoc 
				javadoc = annotationMember.getJavadoc();
				StringBuilder sb = new StringBuilder();
				processAnnotationMemberModifier(sb,annotationMember.modifiers());
				sb.append(annotationMember.getType());
				sb.append(" ");
				sb.append(annotationMember.getName().toString());
				sb.append("() ");
				if(annotationMember.getDefault() != null){
					sb.append("default ");
					sb.append(annotationMember.getDefault().toString());
				}
				sb.append(";");
				method.setMethodLine(sb.toString());
			}else if(obj instanceof EnumDeclaration){
				method.setMethodLine(obj.toString().trim()+";");
				EnumDeclaration enumDeclaration = (EnumDeclaration) obj;
				javadoc = enumDeclaration.getJavadoc();
			}else if(obj instanceof FieldDeclaration){
				method.setMethodLine(obj.toString().trim());
				FieldDeclaration fieldDeclaration = (FieldDeclaration)obj;
				javadoc = fieldDeclaration.getJavadoc();
			}
			if(javadoc != null){
				List<String> docLines = parseDocLine(javadoc.toString());
				for(String comment:docLines){
					method.addJavaDocLine(comment);
				}
			}
		}
	}

	private void processAnnotationMemberModifier(StringBuilder sb, List<Modifier> modifiers){
		for(Modifier modifier:modifiers){
			if(modifier.isPublic()){
				sb.append("public ");
			}
		}
	}

	private void processAnotationModifier(VirjarAnnotation anotation,Modifier modifier){
		if(modifier.isFinal()){
			anotation.setFinal(true);
		}else if(modifier.isNative()){
			throw new SourceFileNotSupportException("not support native modifier");
		}else if(modifier.isPrivate()){
			anotation.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			anotation.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			anotation.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			anotation.setStatic(true);
		}else if(modifier.isSynchronized()){
			throw new SourceFileNotSupportException("not support synchroized modifier");
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			throw new SourceFileNotSupportException("not support abstract modifier");
		}
	}

	private void addImports(VirjarAnnotation anotation,CompilationUnit compilationUnit){
		List<ImportDeclaration> imports = compilationUnit.imports();
		for(ImportDeclaration importDeclaration : imports){
			anotation.addImportedType(new FullyQualifiedJavaType(importDeclaration.getName().getFullyQualifiedName()));
		}
	}

	public Interface parseInterface(InputStream is,String clazzPackage) throws IOException{
//		this.is = is;
//		this.is = is;
		CompilationUnit compilationUnit = JDTParse(is);

		String typeName ="";
		List<String> supperInterfaces = new ArrayList<String>();
		List types = compilationUnit.types();
		TypeDeclaration clazz;

		if(types != null && types.size()>0){
			clazz = (TypeDeclaration) types.get(0);
			typeName = clazz.getName().getFullyQualifiedName();
			typeName = clazz.getName().getFullyQualifiedName();
			for(Object obj :clazz.superInterfaceTypes()){
				if(obj instanceof TypeDeclaration){
					TypeDeclaration supperInterface = (TypeDeclaration) obj;
					supperInterfaces.add(supperInterface.getName().getFullyQualifiedName());
				}else{
					throw new RuntimeException("could not support convert "+obj.getClass());
				}
			}
		}else{
			return null;
		}
		//package处理
		Interface answer = new Interface(clazzPackage+"."+typeName);

		//父类处理
		for(String supperInterface:supperInterfaces){
			answer.addSuperInterface(new FullyQualifiedJavaType(supperInterface));
		}

		//import处理
		addImports(answer,compilationUnit);

		//class注释处理
		Javadoc javadoc = clazz.getJavadoc();
		if(javadoc != null){
			List<String> docLines = parseDocLine(javadoc.toString());
			for(String comment:docLines){
				answer.addJavaDocLine(comment);
			}
		}

		//注解处理
		List modifiers = clazz.modifiers();
		for(Object obj: modifiers){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				answer.addAnnotation(annotation.toString());
			}else if(obj instanceof Modifier){
				processInterfaceModifier(answer,(Modifier)obj);
			}
		}

		//接口处理

		for(Object obj:clazz.superInterfaceTypes()){
			if(obj instanceof SimpleType){
				SimpleType interfaceType = (SimpleType) obj;
				answer.addSuperInterface(new FullyQualifiedJavaType(interfaceType.getName().getFullyQualifiedName()));
			}else{
				System.out.println(obj.getClass()+"---"+obj);
			}
		}



		//method处理
		parseMethod(clazz.getMethods(),answer);
		return answer;
	}

	private void parseMethod(MethodDeclaration[] methods, Interface interfaze) {
		// TODO Auto-generated method stub
		for(MethodDeclaration method:methods){

			Method mybatisMethod = null;
			//泛型方法
			List<TypeParameter> typeParameters = method.typeParameters();
			if(typeParameters.size() >0){
				mybatisMethod = new VirjarMethod();
				VirjarMethod virjarMethod = (VirjarMethod) mybatisMethod;
				for(TypeParameter parameter:typeParameters){
					virjarMethod.addTypeParameter(parameter.toString());
				}
			}else{
				mybatisMethod = new Method();
			}

			interfaze.addMethod(mybatisMethod);

			//注释处理
			Javadoc javadoc = method.getJavadoc();
			if(javadoc !=null){
				List<String> docLines = parseDocLine(method.getJavadoc().toString());
				for(String line:docLines){
					mybatisMethod.addJavaDocLine(line);
				}
			}

			//注解处理,modifier处理
			for(Object obj : method.modifiers()){
				if(obj instanceof Annotation){
					Annotation annotation = (Annotation) obj;
					mybatisMethod.addAnnotation(annotation.toString());
				}else if(obj instanceof Modifier){
					processMethodModifier(mybatisMethod,(Modifier)obj);
				}
			}

			mybatisMethod.setName(method.getName().getFullyQualifiedName());
			if(method.isConstructor()){
				mybatisMethod.setConstructor(true);
			}else{
				Type returnType = method.getReturnType2();
				if(returnType != null){
					mybatisMethod.setReturnType(new FullyQualifiedJavaType(returnType.toString()));
				}
			}
			//参数列表
			List parameters = method.parameters();
			for(Object obj:parameters){
				SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration)obj;
				Parameter parameter = new Parameter(new VirjarFullyQualifiedJavaType(singleVariableDeclaration.getType().toString()), singleVariableDeclaration.getName().getFullyQualifiedName(), singleVariableDeclaration.isVarargs());
				mybatisMethod.addParameter(parameter);
				List modifiers = singleVariableDeclaration.modifiers();
				for(Object anotationObj:modifiers){
					if(anotationObj instanceof Annotation){
						Annotation annotation = (Annotation)anotationObj;
						parameter.addAnnotation(annotation.toString());
					}
				}
			}
		}
		//processBodyLine(method.getBody().toString(), mybatisMethod);
	}

	private void processInterfaceModifier(Interface interfaze, Modifier modifier) {
		// TODO Auto-generated method stub
		if(modifier.isFinal()){
			interfaze.setFinal(true);
		}else if(modifier.isNative()){
			throw new SourceFileNotSupportException("not support native modifier");
		}else if(modifier.isPrivate()){
			interfaze.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			interfaze.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			interfaze.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			interfaze.setStatic(true);
		}else if(modifier.isSynchronized()){
			throw new SourceFileNotSupportException("not support synchroized modifier");
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			throw new SourceFileNotSupportException("not support abstract modifier");
		}
	}

	private void addImports(Interface answer, CompilationUnit compilationUnit) {
		// TODO Auto-generated method stub
		List<ImportDeclaration> imports = compilationUnit.imports();
		for(ImportDeclaration importDeclaration : imports){
			answer.addImportedType(new FullyQualifiedJavaType(importDeclaration.getName().getFullyQualifiedName()));
		}
	}

	public  TopLevelClass parseClass(InputStream is,String clazzPackage) throws IOException{
		//this.is = is;
		CompilationUnit compilationUnit = JDTParse(is);

		String typeName ="";
		Type superclassType = null;
		List<String> supperInterfaces = new ArrayList<String>();
		List<String> typeParameters = new ArrayList<String>();
		List types = compilationUnit.types();
		TypeDeclaration clazz;

		if(types != null && types.size()>0){
			clazz = (TypeDeclaration) types.get(0);
			typeName = clazz.getName().getFullyQualifiedName();
			superclassType = clazz.getSuperclassType();
			for(Object obj :clazz.superInterfaceTypes()){
				if(obj instanceof SimpleType){
					SimpleType supperInterface = (SimpleType) obj;
					supperInterfaces.add(supperInterface.getName().getFullyQualifiedName());
				}else{
					throw new RuntimeException("could not support convert "+obj.getClass());
				}
			}
			//范型数据获取
			for(Object obj:clazz.typeParameters()){
				if(obj instanceof TypeParameter){
					TypeParameter typeParameter = (TypeParameter) obj;
					typeParameters.add(typeParameter.getName().getFullyQualifiedName());
				}
			}
		}else{
			return null;
		}
		//package处理
		ParameterTopLevelClass topLevelClass  = new ParameterTopLevelClass(clazzPackage+"."+typeName);

		//父类处理
		if(superclassType != null){
			topLevelClass.setSuperClass(superclassType.toString());
		}
		for(String supperInterface:supperInterfaces){
			topLevelClass.addSuperInterface(new FullyQualifiedJavaType(supperInterface));
		}

		//范型处理
		//topLevelClass
		if(typeParameters.size() >0){
			for(String typeParameter:typeParameters){
				topLevelClass.addTypeArgument(typeParameter);
			}
		}

		//import处理
		addImports(topLevelClass,compilationUnit);

		//class注释处理
		Javadoc javadoc = clazz.getJavadoc();
		if(javadoc != null){
			List<String> docLines = parseDocLine(javadoc.toString());
			for(String comment:docLines){
				topLevelClass.addJavaDocLine(comment);
			}
		}

		//注解处理
		List modifiers = clazz.modifiers();
		for(Object obj: modifiers){
			if(obj instanceof Annotation){
				Annotation annotation = (Annotation) obj;
				topLevelClass.addAnnotation(annotation.toString());
			}else if(obj instanceof Modifier){
				processClassModifier(topLevelClass,(Modifier)obj);
			}
		}

		//接口处理

		for(Object obj:clazz.superInterfaceTypes()){
			if(obj instanceof SimpleType){
				SimpleType interfaceType = (SimpleType) obj;
				topLevelClass.addSuperInterface(new FullyQualifiedJavaType(interfaceType.getName().getFullyQualifiedName()));
			}else{
				System.out.println(obj.getClass()+"---"+obj);
			}
		}


		//field 处理
		FieldDeclaration[] fields = clazz.getFields();
		for(FieldDeclaration filedDeclaration:fields){
			Field field = new Field();
			topLevelClass.addField(field);
			field.setType(new FullyQualifiedJavaType(filedDeclaration.getType().toString()));
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) filedDeclaration.fragments().get(0);
			field.setName(fragment.getName().getFullyQualifiedName());
			if(fragment.getInitializer() != null)
				field.setInitializationString(fragment.getInitializer().toString());
			List fieldModifiers = filedDeclaration.modifiers();
			for(Object obj:fieldModifiers){
				if(obj instanceof Annotation){
					Annotation annotation = (Annotation) obj;
					field.addAnnotation(annotation.toString());
				}else if(obj instanceof Modifier){
					processFieldModifier(field,(Modifier)obj);
				}
			}
		}

		//method处理
		parseMethod(clazz.getMethods(),topLevelClass);
		return topLevelClass;
	}


	private void parseMethod(MethodDeclaration[] methods, TopLevelClass topLevelClass){
		for(MethodDeclaration method:methods){

			Method mybatisMethod = null;
			//泛型方法
			List<TypeParameter> typeParameters = method.typeParameters();
			if(typeParameters.size() >0){
				mybatisMethod = new VirjarMethod();
				VirjarMethod virjarMethod = (VirjarMethod) mybatisMethod;
				for(TypeParameter parameter:typeParameters){
					virjarMethod.addTypeParameter(parameter.toString());
				}
			}else{
				mybatisMethod = new Method();
			}

			topLevelClass.addMethod(mybatisMethod);

			//注释处理
			Javadoc javadoc = method.getJavadoc();
			if(javadoc !=null){
				List<String> docLines = parseDocLine(method.getJavadoc().toString());
				for(String line:docLines){
					mybatisMethod.addJavaDocLine(line);
				}
			}

			//注解处理,modifier处理
			for(Object obj : method.modifiers()){
				if(obj instanceof Annotation){
					Annotation annotation = (Annotation) obj;
					mybatisMethod.addAnnotation(annotation.toString());
				}else if(obj instanceof Modifier){
					processMethodModifier(mybatisMethod,(Modifier)obj);
				}
			}

			mybatisMethod.setName(method.getName().getFullyQualifiedName());
			if(method.isConstructor()){
				mybatisMethod.setConstructor(true);
			}else{
				Type returnType = method.getReturnType2();
				if(returnType != null){
					mybatisMethod.setReturnType(new FullyQualifiedJavaType(returnType.toString()));
				}
			}
			//参数列表
			List parameters = method.parameters();
			for(Object obj:parameters){
				SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration)obj;
				Parameter parameter = new Parameter(new VirjarFullyQualifiedJavaType(singleVariableDeclaration.getType().toString()), singleVariableDeclaration.getName().getFullyQualifiedName(), singleVariableDeclaration.isVarargs());
				mybatisMethod.addParameter(parameter);
				List modifiers = singleVariableDeclaration.modifiers();
				for(Object anotationObj:modifiers){
					if(anotationObj instanceof Annotation){
						Annotation annotation = (Annotation)anotationObj;
						parameter.addAnnotation(annotation.toString());
					}
				}
			}
			
			//异常
			List thrownExceptionTypes = method.thrownExceptionTypes();
			for(Object obj:thrownExceptionTypes){
				if(obj instanceof SimpleType){
					SimpleType simpleType = (SimpleType) obj;
					mybatisMethod.addException(new FullyQualifiedJavaType(simpleType.getName().getFullyQualifiedName()));
				}else{
					throw new CodegenException("not support type:"+obj.getClass());
				}
			}

			processBodyLine(method.getBody().toString(), mybatisMethod);
		}
	}

	private void processBodyLine(String body, Method method){
		if(body.matches("\\s*\\{\\s*\\}\\s*")){
			//根据Mybatis CodeGen的定义，如果方法体为空，判定方法为native方法或者abstract方法
			method.addBodyLine("");
			return;
		}
		body = body.substring(2, body.length()-3);//去除首尾括号
		String[] split = body.split("\\n");
		for(String line:split){
			method.addBodyLine(line.trim());
		}
	}

	private void processFieldModifier(Field mybatisField,Modifier modifier){
		if(modifier.isFinal()){
			mybatisField.setFinal(true);
		}else if(modifier.isNative()){
			throw new SourceFileNotSupportException("not support native modifier");
		}else if(modifier.isPrivate()){
			mybatisField.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			mybatisField.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			mybatisField.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			mybatisField.setStatic(true);
		}else if(modifier.isSynchronized()){
			throw new SourceFileNotSupportException("not support synchroized modifier");
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			throw new SourceFileNotSupportException("not support abstract modifier");
		}
	}


	private void processClassModifier(TopLevelClass mybatisClass,Modifier modifier){
		if(modifier.isFinal()){
			mybatisClass.setFinal(true);
		}else if(modifier.isNative()){
			throw new SourceFileNotSupportException("not support native modifier");
		}else if(modifier.isPrivate()){
			mybatisClass.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			mybatisClass.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			mybatisClass.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			mybatisClass.setStatic(true);
		}else if(modifier.isSynchronized()){
			throw new SourceFileNotSupportException("not support synchroized modifier");
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			mybatisClass.setAbstract(true);
		}
	}

	private void processMethodModifier(Method mybatisMethod,Modifier modifier){
		if(modifier.isFinal()){
			mybatisMethod.setFinal(true);
		}else if(modifier.isNative()){
			mybatisMethod.setNative(true);
		}else if(modifier.isPrivate()){
			mybatisMethod.setVisibility(JavaVisibility.PRIVATE);
		}else if(modifier.isProtected()){
			mybatisMethod.setVisibility(JavaVisibility.PROTECTED);
		}else if(modifier.isPublic()){
			mybatisMethod.setVisibility(JavaVisibility.PUBLIC);
		}else if(modifier.isStatic()){
			mybatisMethod.setStatic(true);
		}else if(modifier.isSynchronized()){
			mybatisMethod.setSynchronized(true);
		}else if(modifier.isStrictfp()){
			throw new SourceFileNotSupportException("not support strictfp modifier");
		}else if(modifier.isTransient()){
			throw new SourceFileNotSupportException("not support transient modifier");
		}else if(modifier.isVolatile()){
			throw new SourceFileNotSupportException("not support volatile modifier");
		}else if(modifier.isAbstract()){
			throw new SourceFileNotSupportException("not support abstract modifier");
		}
	}

	/**
	 * 从一个代码注释块取出单行注释，去掉注释标记，去掉行首星号（*）
	 */
	private List<String> parseDocLine(String commentLines){
		Matcher matcher = doclinePattern.matcher(commentLines);
		List<String> answer  = new ArrayList<String>();
		while(matcher.find()){
			answer.add(matcher.group(1));
		}
		return answer;
	}

	private void addImports(TopLevelClass topLevelClass,CompilationUnit compilationUnit){
		List<ImportDeclaration> imports = compilationUnit.imports();
		for(ImportDeclaration importDeclaration : imports){
			topLevelClass.addImportedType(importDeclaration.getName().getFullyQualifiedName());
		}
	}

	private  CompilationUnit JDTParse(String source){
		this.astParser.setSource(source.toCharArray());
		return (CompilationUnit) this.astParser.createAST(null);
	}

	private  CompilationUnit JDTParse(InputStream is){
		try {
			return JDTParse(IOUtils.toString(is));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SourceFileNotSupportException(e);
		}finally{
			try{
				is.close();
			}catch(IOException eio){
				eio.printStackTrace();
				throw new SourceFileNotSupportException(eio);
			}
		}
	}
}
