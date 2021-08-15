package cn.edu.scu.virjarjcd.mybatis.formater.java;

import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.config.Context;

import de.hunsicker.jalopy.Jalopy;

public class JalopyCodeParser implements JavaFormatter {

	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFormattedContent(CompilationUnit compilationUnit) {
		// TODO Auto-generated method stub
		Jalopy jalopy = new Jalopy();
		jalopy.setInput(compilationUnit.getFormattedContent(), compilationUnit.getType().getFullyQualifiedName()+".java");
		StringBuffer sb = new StringBuffer();
		jalopy.setOutput(sb);
		jalopy.format();
		return sb.toString();
	}

}
