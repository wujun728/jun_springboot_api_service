package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import java.util.Set;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class AddGenericMapperPlugin extends PluginAdapter
{
  private String searchString;
  private String criteriaClassName;

  public boolean validate(List<String> warnings)
  {
    this.searchString = this.properties.getProperty("searchString");
    this.criteriaClassName = this.properties.getProperty("criteriaClassName");

    boolean valid = (StringUtility.stringHasValue(this.searchString)) && (StringUtility.stringHasValue(this.criteriaClassName));

    if (!valid) {
      if (!StringUtility.stringHasValue(this.searchString)) {
        warnings.add(Messages.getString("ValidationError.18", "AddGenericMapperPlugin", "searchString"));
      }
      if (!StringUtility.stringHasValue(this.criteriaClassName)) {
        warnings.add(Messages.getString("ValidationError.18", "AddGenericMapperPlugin", "criteriaClassName"));
      }
    }

    return valid;
  }

  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    interfaze.getMethods().clear();

    interfaze.addImportedType(new FullyQualifiedJavaType("com.hkntv.pylon.data.common.annotation.Mapper"));
    interfaze.addAnnotation("@Mapper");

    Set<FullyQualifiedJavaType> fullyQualifiedJavaTypeSet = interfaze.getSuperInterfaceTypes();
    if ((fullyQualifiedJavaTypeSet != null) && (fullyQualifiedJavaTypeSet.size() > 0)) {
      FullyQualifiedJavaType[] fullyQualifiedJavaTypes = new FullyQualifiedJavaType[fullyQualifiedJavaTypeSet.size()];
      fullyQualifiedJavaTypeSet.toArray(fullyQualifiedJavaTypes);
      fullyQualifiedJavaTypes[0].addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
      fullyQualifiedJavaTypes[0].addTypeArgument(new FullyQualifiedJavaType(interfaze.getType().getShortName().replaceAll(this.searchString, this.criteriaClassName)));
      fullyQualifiedJavaTypes[0].addTypeArgument(((IntrospectedColumn)introspectedTable.getPrimaryKeyColumns().get(0)).getFullyQualifiedJavaType());
    }

    return true;
  }
}
