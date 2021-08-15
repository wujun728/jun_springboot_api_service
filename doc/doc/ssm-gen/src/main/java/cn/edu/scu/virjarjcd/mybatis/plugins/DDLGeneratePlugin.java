package cn.edu.scu.virjarjcd.mybatis.plugins;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.db.ConnectionFactory;

import cn.edu.scu.virjarjcd.exception.CodegenException;

public class DDLGeneratePlugin extends PluginAdapter
{
  private String dbsqlPath;

  public String getDbsqlPath()
  {
    return this.dbsqlPath;
  }

  public void setDbsqlPath(String dbsqlPath) {
    this.dbsqlPath = dbsqlPath;
  }

  public boolean validate(List<String> warnings)
  {
    this.dbsqlPath = System.getProperty("dbsqlPath");
    if (StringUtils.isEmpty(this.dbsqlPath)) {
      throw new CodegenException("Failed to get db.sql path from system properties");
    }
    return true;
  }

  public void initialized(IntrospectedTable introspectedTable)
  {
    String ddl = "";
    try {
      Connection connection = ConnectionFactory.getInstance().getConnection(introspectedTable.getContext().getJdbcConnectionConfiguration());
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("show create table " + introspectedTable.getFullyQualifiedTableNameAtRuntime());
      while (rs.next()) {
        ddl = rs.getString(2);
      }

      FileUtils.writeStringToFile(new File(this.dbsqlPath), ddl, "UTF-8", true);
    } catch (SQLException e) {
      throw new CodegenException("Failed to get ddl from database", e);
    } catch (IOException e) {
      throw new CodegenException("Failed to generate ddl for db.sql", e);
    }
  }
}
