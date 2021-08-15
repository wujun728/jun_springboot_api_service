package cn.edu.scu.virjarjcd.mybatis.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

public class RenameMapperClassPlugin extends PluginAdapter
{
  private String searchString;
  private String replaceString;
  private Pattern pattern;
  private Pattern patternParam;

  public boolean validate(List<String> warnings)
  {
    this.searchString = this.properties.getProperty("searchString");
    this.replaceString = this.properties.getProperty("replaceString");

    boolean valid = (StringUtility.stringHasValue(this.searchString)) && (StringUtility.stringHasValue(this.replaceString));

    if (valid) {
      this.pattern = Pattern.compile(this.searchString);
    } else {
      if (!StringUtility.stringHasValue(this.searchString)) {
        warnings.add(Messages.getString("ValidationError.18", "RenameMapperClassPlugin", "searchString"));
      }

      if (!StringUtility.stringHasValue(this.replaceString)) {
        warnings.add(Messages.getString("ValidationError.18", "RenameMapperClassPlugin", "replaceString"));
      }

    }

    this.patternParam = Pattern.compile("record");

    return valid;
  }

  public void initialized(IntrospectedTable introspectedTable)
  {
    String oldType = introspectedTable.getMyBatis3JavaMapperType();
    Matcher matcher = this.pattern.matcher(oldType);
    oldType = matcher.replaceAll(this.replaceString);

    introspectedTable.setMyBatis3JavaMapperType(oldType);
  }

  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    //interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.data.repository.query.Param"));

    List<Method> methods = interfaze.getMethods();

    if ((methods != null) && (methods.size() >= 0)) {
      for (Method method : methods) {
        List<Parameter> params = method.getParameters();
        List<Parameter> paramList = new ArrayList<Parameter>();
        if ((params != null) && (params.size() >= 0)) {
          for (Parameter parameter : params) {
            Parameter param = new Parameter(parameter.getType(), rename(parameter.getName(), parameter.getType().getShortName().toLowerCase()), parameter.isVarargs());

            if ((parameter.getAnnotations() != null) && (parameter.getAnnotations().size() >= 0)) {
              for (String annotation : parameter.getAnnotations()) {
                param.addAnnotation(annotation);
              }
            }

            //param.addAnnotation("@Param(\"" + param.getName() + "\")");

            paramList.add(param);
          }
        }
        method.getParameters().clear();
        method.getParameters().addAll(paramList);
      }
    }

    return true;
  }

  private String rename(String name, String replace) {
    Matcher matcher = this.patternParam.matcher(name);
    return matcher.replaceAll(replace);
  }
}
