package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class RenameClientClassNamePlugin extends PluginAdapter
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
        warnings.add(Messages.getString("ValidationError.18", "RenameClientClassNamePlugin", "searchString"));
      }
      if (!StringUtility.stringHasValue(this.replaceString)) {
        warnings.add(Messages.getString("ValidationError.18", "RenameClientClassNamePlugin", "replaceString"));
      }
    }

    return valid;
  }

  public void initialized(IntrospectedTable introspectedTable)
  {
    introspectedTable.setMyBatis3JavaMapperType(rename(introspectedTable.getMyBatis3JavaMapperType()));
  }

  private String rename(String name) {
    Matcher matcher = this.pattern.matcher(name);
    return matcher.replaceAll(this.replaceString);
  }
}
