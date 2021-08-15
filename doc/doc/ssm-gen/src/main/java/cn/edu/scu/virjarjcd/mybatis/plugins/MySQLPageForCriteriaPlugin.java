package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

public class MySQLPageForCriteriaPlugin extends PluginAdapter
{
  public boolean validate(List<String> warnings)
  {
    return true;
  }

  public boolean providerSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    method.getBodyLines().clear();

    method.addBodyLine("BEGIN();");
    method.addBodyLine("if (example != null && example.isCustomizeSelect()) {");

    StringBuilder sbIf = new StringBuilder();
    StringBuilder sbElse = new StringBuilder();
    boolean distinctCheck = true;

    List<IntrospectedColumn> introspectedColumnList = introspectedTable.getNonBLOBColumns();
    if ((introspectedColumnList == null) || (introspectedColumnList.size() <= 0)) {
      return false;
    }

    for (IntrospectedColumn introspectedColumn : introspectedColumnList) {
      if (distinctCheck) {
        sbIf.append(new StringBuilder().append("if (example.is").append(JavaBeansUtil.getCamelCaseString(introspectedColumn.getJavaProperty(), true)).append("()) {").toString());
        sbIf.append("if (example.isDistinct()) {");
        sbIf.append(String.format("SELECT_DISTINCT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
        sbIf.append("} else {");
        sbIf.append(String.format("SELECT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
        sbIf.append("}");
        sbIf.append("}");

        sbElse.append("if (example != null && example.isDistinct()) {");
        sbElse.append(String.format("SELECT_DISTINCT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
        sbElse.append("} else {");
        sbElse.append(String.format("SELECT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
        sbElse.append("}");
      } else {
        sbIf.append(new StringBuilder().append("if (example.is").append(JavaBeansUtil.getCamelCaseString(introspectedColumn.getJavaProperty(), true)).append("()) {").toString());
        sbIf.append(String.format("SELECT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
        sbIf.append("}");

        sbElse.append(String.format("SELECT(\"%s\");", new Object[] { StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)) }));
      }

      distinctCheck = false;
    }

    method.addBodyLine(sbIf.toString());
    method.addBodyLine("}");
    method.addBodyLine("else {");
    method.addBodyLine(sbElse.toString());
    method.addBodyLine("}");
    method.addBodyLine(String.format("FROM(\"%s\");", new Object[] { StringUtility.escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()) }));
    method.addBodyLine("applyWhere(example, false);");

    method.addBodyLine("");
    method.addBodyLine("StringBuilder sb = new StringBuilder();");
    method.addBodyLine("if (example != null && example.getOrderByClause() != null) {");
    method.addBodyLine("ORDER_BY(example.getOrderByClause());");
    method.addBodyLine("sb.append(SQL());");
    method.addBodyLine("if (example.getPageNum() >= 0 && example.getPageSize() > 0) {");
    method.addBodyLine("sb.append(\" limit #{offset}, #{pageSize}\");");
    method.addBodyLine("}");
    method.addBodyLine("}");
    method.addBodyLine("else {");
    method.addBodyLine("sb.append(SQL());");
    method.addBodyLine("}");

    method.addBodyLine("");
    method.addBodyLine("return sb.toString();");

    return true;
  }

  public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable)
  {
    element.getElements().clear();

    element.addElement(new TextElement("select"));

    XmlElement chooseElement = new XmlElement("choose");
    element.addElement(chooseElement);

    XmlElement isCustomizeSelectElementIf = new XmlElement("when");
    XmlElement isCustomizeSelectElementElse = new XmlElement("otherwise");

    chooseElement.addElement(isCustomizeSelectElementIf);
    chooseElement.addElement(isCustomizeSelectElementElse);

    isCustomizeSelectElementIf.addAttribute(new Attribute("test", "customizeSelect"));

    XmlElement ifDisElement = new XmlElement("if");
    ifDisElement.addAttribute(new Attribute("test", "distinct"));
    ifDisElement.addElement(new TextElement("distinct"));

    isCustomizeSelectElementElse.addElement(ifDisElement);

    StringBuilder sb = new StringBuilder();
    if (StringUtility.stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
      sb.append('\'');
      sb.append(introspectedTable.getSelectByExampleQueryId());
      sb.append("' as QUERYID,");
      isCustomizeSelectElementElse.addElement(new TextElement(sb.toString()));
    }
    isCustomizeSelectElementElse.addElement(getBaseColumnListElement(introspectedTable));

    isCustomizeSelectElementIf.addElement(new TextElement("${selectFields}"));

    sb.setLength(0);
    sb.append("from ");
    sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());

    element.addElement(new TextElement(sb.toString()));
    element.addElement(getExampleIncludeElement(introspectedTable));

    XmlElement ifOrderByElement = new XmlElement("if");
    ifOrderByElement.addAttribute(new Attribute("test", "orderByClause != null"));
    ifOrderByElement.addElement(new TextElement("order by ${orderByClause}"));

    XmlElement limitElement = new XmlElement("if");
    limitElement.addAttribute(new Attribute("test", "pageNum >= 0 and pageSize > 0"));
    limitElement.addElement(new TextElement("limit ${offset}, ${pageSize}"));
    ifOrderByElement.addElement(limitElement);

    element.addElement(ifOrderByElement);

    return true;
  }

  protected XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable)
  {
    XmlElement answer = new XmlElement("include");
    answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
    return answer;
  }

  protected XmlElement getExampleIncludeElement(IntrospectedTable introspectedTable) {
    XmlElement ifElement = new XmlElement("if");
    ifElement.addAttribute(new Attribute("test", "_parameter != null"));

    XmlElement includeElement = new XmlElement("include");
    includeElement.addAttribute(new Attribute("refid", introspectedTable.getExampleWhereClauseId()));
    ifElement.addElement(includeElement);

    return ifElement;
  }
}
