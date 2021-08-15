package cn.edu.scu.virjarjcd.mybatis;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class CommentGenerator extends DefaultCommentGenerator {

	@Override
	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		super.addClassComment(innerClass, introspectedTable);
	}

	@Override
	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		// TODO Auto-generated method stub
		super.addClassComment(innerClass, introspectedTable, markAsDoNotDelete);
	}

}
