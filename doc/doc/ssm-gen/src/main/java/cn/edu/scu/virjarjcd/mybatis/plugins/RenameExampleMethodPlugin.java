package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class RenameExampleMethodPlugin extends PluginAdapter
{
  private String searchString;
  private String replaceString;
  private Pattern pattern;

  public boolean validate(List<String> warnings)
  {
    this.searchString = this.properties.getProperty("searchString");
    this.replaceString = this.properties.getProperty("replaceString");

    boolean valid = (StringUtility.stringHasValue(this.searchString)) && (StringUtility.stringHasValue(this.replaceString));

    if (valid) {
      this.pattern = Pattern.compile(this.searchString);
    } else {
      if (!StringUtility.stringHasValue(this.searchString)) {
        warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "searchString"));
      }
      if (!StringUtility.stringHasValue(this.replaceString)) {
        warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "replaceString"));
      }
    }

    return valid;
  }

  public void initialized(IntrospectedTable introspectedTable)
  {
    introspectedTable.setCountByExampleStatementId(rename(introspectedTable.getCountByExampleStatementId()));
    introspectedTable.setDeleteByExampleStatementId(rename(introspectedTable.getDeleteByExampleStatementId()));

    introspectedTable.setSelectByExampleStatementId(rename(introspectedTable.getSelectByExampleStatementId()));
    introspectedTable.setSelectByExampleWithBLOBsStatementId(rename(introspectedTable.getSelectByExampleWithBLOBsStatementId()));

    introspectedTable.setUpdateByExampleStatementId(rename(introspectedTable.getUpdateByExampleStatementId()));
    introspectedTable.setUpdateByExampleSelectiveStatementId(rename(introspectedTable.getUpdateByExampleSelectiveStatementId()));

    introspectedTable.setUpdateByExampleWithBLOBsStatementId(rename(introspectedTable.getUpdateByExampleWithBLOBsStatementId()));

    introspectedTable.setExampleWhereClauseId(rename(introspectedTable.getExampleWhereClauseId()));

    introspectedTable.setMyBatis3UpdateByExampleWhereClauseId(rename(introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
  }

  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    return true;
  }

  public boolean providerGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    renameMethod(topLevelClass.getMethods());
    return true;
  }

  private void renameMethod(List<Method> methods) {
    if ((methods != null) && (methods.size() > 0))
      for (Method method : methods)
        method.setName(rename(method.getName()));
  }

  private String rename(String name)
  {
    Matcher matcher = this.pattern.matcher(name);
    return matcher.replaceAll(this.replaceString);
  }
}
