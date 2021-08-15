package cn.edu.scu.virjarjcd.codegertator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.config.PropertyRegistry;

import cn.edu.scu.virjarjcd.feature.model.ModeGerator;
import cn.edu.scu.virjarjcd.feature.vo.VoGerator;

/**
 * 
 * 自定义自省器 通过mybatis generator 原声的接口，不容易复用关于java文件生成的API。本类继承原声IntrospectedTableMyBatis3Impl
 * 并调用代码扩展的代码生成器
 * @author 邓维佳
 */
public class IntrospectedTableVirjarImpl extends IntrospectedTableMyBatis3Impl {

	private List<VirjarFileGenerator> addtinaalJavaModelGerator = new ArrayList<VirjarFileGenerator>();

	@Override
	protected void calculateJavaModelGenerators(List<String> warnings,
			ProgressCallback progressCallback) {
		// TODO Auto-generated method stub

		ModeGerator modeGerator = new ModeGerator();
		super.initializeAbstractGenerator(modeGerator, warnings, progressCallback);
		addtinaalJavaModelGerator.add(modeGerator);

		VoGerator voGerator = new VoGerator();
		super.initializeAbstractGenerator(voGerator, warnings, progressCallback);
		addtinaalJavaModelGerator.add(voGerator);

		
		super.calculateJavaModelGenerators(warnings, progressCallback);
	}

	@Override
	public List<GeneratedJavaFile> getGeneratedJavaFiles() {
		// TODO Auto-generated method stub
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
		for (VirjarFileGenerator javaGenerator : addtinaalJavaModelGerator) {
			List<CompilationUnit> compilationUnits = javaGenerator
					.getCompilationUnits();
			for (CompilationUnit compilationUnit : compilationUnits) {
				GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
						javaGenerator.targetProject(),
						context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
						context.getJavaFormatter());
				answer.add(gjf);
			}
		}

		answer.addAll(super.getGeneratedJavaFiles());
		return answer;
	}




}
