package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class MySQLPagePlugin extends PluginAdapter
{
  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));

    genSelectCountMethod(interfaze, introspectedTable);
    genSelectPageMethod(interfaze, introspectedTable);

    return true;
  }

  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable)
  {
    addselectCountElements(document.getRootElement(), introspectedTable);
    addselectPageElements(document.getRootElement(), introspectedTable);
    return true;
  }

  public boolean validate(List<String> warnings)
  {
    return true;
  }

  public void addselectCountElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
    XmlElement answer = new XmlElement("select");

    answer.addAttribute(new Attribute("id", "selectCount"));
    answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
    answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));

    StringBuilder sb = new StringBuilder();
    sb.append("select count(*) from ");
    sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());

    answer.addElement(new TextElement(sb.toString()));
    XmlElement whereElement = new XmlElement("where");
    for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");

      XmlElement insertNotNullElement = new XmlElement("if");
      insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

      sb.setLength(0);
      sb.append("and ");
      sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

      insertNotNullElement.addElement(new TextElement(sb.toString()));
      whereElement.addElement(insertNotNullElement);
    }

    answer.addElement(whereElement);
    parentElement.addElement(answer);
  }

  public void addselectPageElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
    XmlElement answer = new XmlElement("select");

    answer.addAttribute(new Attribute("id", "selectPage"));
    answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

    StringBuilder sb = new StringBuilder();
    sb.append("select ");
    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(getBaseColumnListElement(introspectedTable));

    sb.setLength(0);
    sb.append("from ");
    sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    sb.append(" where ");
    sb.append("1 = 1 ");
    answer.addElement(new TextElement(sb.toString()));

    for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
      sb.setLength(0);
      sb.append(new StringBuilder().append("param1.").append(introspectedColumn.getJavaProperty()).toString());
      sb.append(" != null");

      XmlElement insertNotNullElement = new XmlElement("if");
      insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

      sb.setLength(0);
      sb.append("and ");
      sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "param1."));

      insertNotNullElement.addElement(new TextElement(sb.toString()));
      answer.addElement(insertNotNullElement);
    }

    XmlElement ifSortNotNull = new XmlElement("if");
    ifSortNotNull.addAttribute(new Attribute("test", "param2.sort != null"));
    ifSortNotNull.addElement(new TextElement("order by "));

    XmlElement forSort = new XmlElement("foreach");
    forSort.addAttribute(new Attribute("collection", "param2.sort"));
    forSort.addAttribute(new Attribute("item", "order"));
    forSort.addAttribute(new Attribute("separator", ","));
    forSort.addElement(new TextElement("${order.property} ${order.direction}"));
    ifSortNotNull.addElement(forSort);
    answer.addElement(ifSortNotNull);

    XmlElement ifPage = new XmlElement("if");
    ifPage.addAttribute(new Attribute("test", "param2.offset >= 0 and param2.pageSize > 0"));
    ifPage.addElement(new TextElement("limit ${param2.offset}, ${param2.pageSize}"));
    answer.addElement(ifPage);

    parentElement.addElement(answer);
  }

  private void genSelectCountMethod(Interface interfaze, IntrospectedTable introspectedTable) {
    FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.setName("selectCount");

    Parameter parameter = new Parameter(fullyQualifiedJavaType, fullyQualifiedJavaType.getShortName().toLowerCase());
    //parameter.addAnnotation(new StringBuilder().append("@Param(\"").append(parameter.getName()).append("\")").toString());

    method.addParameter(parameter);

    interfaze.addImportedType(fullyQualifiedJavaType);
    interfaze.addMethod(method);
  }

  private void genSelectPageMethod(Interface interfaze, IntrospectedTable introspectedTable) {
    FullyQualifiedJavaType param1Type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    FullyQualifiedJavaType param2Type = new FullyQualifiedJavaType("org.springframework.data.domain.Pageable");

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
    FullyQualifiedJavaType listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    returnType.addTypeArgument(listType);
    method.setReturnType(returnType);
    method.setName("selectPage");

    Parameter parameter1 = new Parameter(param1Type, param1Type.getShortName().toLowerCase());
    //parameter1.addAnnotation(new StringBuilder().append("@Param(\"").append(parameter1.getName()).append("\")").toString());
    method.addParameter(parameter1);

    Parameter parameter2 = new Parameter(param2Type, param2Type.getShortName().toLowerCase());
    //parameter2.addAnnotation(new StringBuilder().append("@Param(\"").append(parameter2.getName()).append("\")").toString());
    method.addParameter(parameter2);

    interfaze.addImportedType(returnType);
    interfaze.addImportedType(listType);
    interfaze.addImportedType(param1Type);
    interfaze.addImportedType(param2Type);
    interfaze.addMethod(method);
  }

  private XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable) {
    XmlElement answer = new XmlElement("include");
    answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
    return answer;
  }
}
