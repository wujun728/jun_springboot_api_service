package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class ModelExampleClassExtPlugin extends PluginAdapter
{
  public boolean validate(List<String> warnings)
  {
    return true;
  }

  public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    addLimitFieldAndGetSet(topLevelClass, introspectedTable, "pageNum");
    addLimitFieldAndGetSet(topLevelClass, introspectedTable, "pageSize");

    List<Method> methods = topLevelClass.getMethods();
    Method method = null;
    for (Method m : methods) {
      if (m.getName().equals("clear")) {
        method = m;
        break;
      }
    }

    if (method == null) {
      return false;
    }

    method.addBodyLine("pageNum = -1;");
    method.addBodyLine("pageSize = -1;");

    Method isCustomizeSelectMethod = new Method();
    isCustomizeSelectMethod.setVisibility(JavaVisibility.PUBLIC);
    isCustomizeSelectMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
    isCustomizeSelectMethod.setName("isCustomizeSelect");

    StringBuilder sb = new StringBuilder();
    for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
      addSelectFieldAndGetSet(topLevelClass, introspectedTable, introspectedColumn.getJavaProperty());
      method.addBodyLine(new StringBuilder().append(introspectedColumn.getJavaProperty()).append(" = false;").toString());
      sb.append(new StringBuilder().append(introspectedColumn.getJavaProperty()).append(" | ").toString());
    }

    String isCustomizeSelectBody = sb.toString();
    if (isCustomizeSelectBody.length() == 0) {
      return false;
    }

    isCustomizeSelectMethod.addBodyLine(new StringBuilder().append("return ").append(isCustomizeSelectBody.substring(0, isCustomizeSelectBody.length() - 2)).append(";").toString());
    topLevelClass.addMethod(isCustomizeSelectMethod);

    Method getSelectFieldMethod = new Method();
    getSelectFieldMethod.setVisibility(JavaVisibility.PUBLIC);
    getSelectFieldMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
    getSelectFieldMethod.setName("getSelectFields");
    getSelectFieldMethod.addBodyLine("StringBuilder sb = new StringBuilder();");

    List<IntrospectedColumn> introspectedColumnList = introspectedTable.getNonBLOBColumns();
    if ((introspectedColumnList == null) || (introspectedColumnList.size() <= 0)) {
      return false;
    }

    boolean distinctCheck = true;
    for (IntrospectedColumn introspectedColumn : introspectedColumnList)
    {
      getSelectFieldMethod.addBodyLine(new StringBuilder().append("if (").append(introspectedColumn.getJavaProperty()).append(") {").toString());
      if (distinctCheck) {
        getSelectFieldMethod.addBodyLine("if (distinct)");
        getSelectFieldMethod.addBodyLine("sb.append(\"distinct \");");
      }

      distinctCheck = false;

      getSelectFieldMethod.addBodyLine(new StringBuilder().append("sb.append(\"").append(introspectedColumn.getActualColumnName()).append(", \");").toString());
      getSelectFieldMethod.addBodyLine("}");
    }

    getSelectFieldMethod.addBodyLine("String fields = sb.toString();");
    getSelectFieldMethod.addBodyLine("return fields.substring(0, fields.length() - 2);");
    topLevelClass.addMethod(getSelectFieldMethod);

    Method getOffsetMethod = new Method();
    getOffsetMethod.setVisibility(JavaVisibility.PUBLIC);
    getOffsetMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
    getOffsetMethod.setName("getOffset");
    getOffsetMethod.addBodyLine("return pageNum * pageSize;");
    topLevelClass.addMethod(getOffsetMethod);

    Method getBeginMethod = new Method();
    getBeginMethod.setVisibility(JavaVisibility.PUBLIC);
    getBeginMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
    getBeginMethod.setName("getBegin");
    getBeginMethod.addBodyLine("return pageNum * pageSize + 1;");
    topLevelClass.addMethod(getBeginMethod);

    Method getEndMethod = new Method();
    getEndMethod.setVisibility(JavaVisibility.PUBLIC);
    getEndMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
    getEndMethod.setName("getEnd");
    getEndMethod.addBodyLine("return (pageNum + 1) * pageSize;");
    topLevelClass.addMethod(getEndMethod);

    Method addSelectFieldsMethod = new Method();
    addSelectFieldsMethod.setVisibility(JavaVisibility.PUBLIC);
    addSelectFieldsMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.List<String>"), "fields"));

    addSelectFieldsMethod.setName("addSelectFields");

    StringBuilder sbDynamicFields = new StringBuilder();
    for (IntrospectedColumn introspectedColumn : introspectedTable.getNonBLOBColumns()) {
      sbDynamicFields.append(new StringBuilder().append("if (fields.contains(\"").append(introspectedColumn.getJavaProperty()).append("\")) {").toString());
      sbDynamicFields.append(new StringBuilder().append(introspectedColumn.getJavaProperty()).append(" = true;").toString());
      sbDynamicFields.append("}");
    }

    addSelectFieldsMethod.addBodyLine(sbDynamicFields.toString());
    topLevelClass.addMethod(addSelectFieldsMethod);

    addSelectFieldsMethod = new Method();
    addSelectFieldsMethod.setVisibility(JavaVisibility.PUBLIC);
    addSelectFieldsMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Set<String>"), "fields"));
    addSelectFieldsMethod.setName("addSelectFields");
    addSelectFieldsMethod.addBodyLine("addSelectFields(new ArrayList<String>(fields));");
    topLevelClass.addMethod(addSelectFieldsMethod);
    topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.Set"));

    return true;
  }

  private void addLimitFieldAndGetSet(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name)
  {
    CommentGenerator commentGenerator = this.context.getCommentGenerator();

    Field field = new Field();
    field.setVisibility(JavaVisibility.PROTECTED);
    field.setType(FullyQualifiedJavaType.getIntInstance());
    field.setName(name);
    field.setInitializationString("-1");
    commentGenerator.addFieldComment(field, introspectedTable);
    topLevelClass.addField(field);

    topLevelClass.addMethod(AbstractJavaGenerator.getGetter(field));

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(JavaBeansUtil.getSetterMethodName(field.getName()));
    method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), name));
    method.addBodyLine(new StringBuilder().append("this.").append(name).append(" = ").append(name).append(";").toString());
    commentGenerator.addGeneralMethodComment(method, introspectedTable);
    topLevelClass.addMethod(method);
  }

  private void addSelectFieldAndGetSet(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name) {
    CommentGenerator commentGenerator = this.context.getCommentGenerator();

    Field field = new Field();
    field.setVisibility(JavaVisibility.PRIVATE);
    field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
    field.setName(name);
    field.setInitializationString("false");
    commentGenerator.addFieldComment(field, introspectedTable);
    topLevelClass.addField(field);

    topLevelClass.addMethod(AbstractJavaGenerator.getGetter(field));

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(topLevelClass.getType());
    method.setName(new StringBuilder().append("add").append(JavaBeansUtil.getCamelCaseString(name, true)).toString());

    method.addBodyLine(new StringBuilder().append("this.").append(name).append(" = true;").toString());
    method.addBodyLine("return this;");
    commentGenerator.addGeneralMethodComment(method, introspectedTable);
    topLevelClass.addMethod(method);
  }
}
