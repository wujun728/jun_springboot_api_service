package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ReplaceModelSetGetMethodPlugin extends PluginAdapter
{
  public boolean validate(List<String> warnings)
  {
    return true;
  }

  public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType)
  {
    return false;
  }

  public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType)
  {
    return false;
  }

  public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.AllArgsConstructor"));
    topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Getter"));
    topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.NoArgsConstructor"));
    topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Setter"));

    topLevelClass.addAnnotation("@Setter");
    topLevelClass.addAnnotation("@Getter");
    topLevelClass.addAnnotation("@NoArgsConstructor");
    topLevelClass.addAnnotation("@AllArgsConstructor");

    return true;
  }
}
