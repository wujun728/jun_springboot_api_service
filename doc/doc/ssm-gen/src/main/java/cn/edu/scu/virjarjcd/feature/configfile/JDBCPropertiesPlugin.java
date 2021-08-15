package cn.edu.scu.virjarjcd.feature.configfile;

import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.exception.CodegenException;
import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.db.ConnectionFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class JDBCPropertiesPlugin extends MergePluginAdpter {

    private String reourcepath;
    private String testResourcePath;
    private String webAppMainpath;
    private String username;
    private String password;
    private String url;
    private String driver;
    private String dbsqlPath;
    private String dbconfigFileName = "mysql.properties";//暂时硬编码


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        // TODO Auto-generated method stub
        Properties dbproperties = new Properties();
        dbproperties.put("resource.rdbms.mysql.username", this.username);
        dbproperties.put("resource.rdbms.mysql.password", this.password);
        dbproperties.put("resource.rdbms.mysql.driver", this.driver);
        dbproperties.put("resource.rdbms.mysql.url", this.url);
        try {
            FileUtils.createIfNotExist(webAppMainpath + "resources.local/");
            FileUtils.createIfNotExist(webAppMainpath + "resources.beta/");
            FileUtils.createIfNotExist(webAppMainpath + "resources.prod/");
            FileUtils.createIfNotExist(webAppMainpath + "resources.dev/");
           // dbproperties.store(new FileOutputStream(reourcepath + dbconfigFileName), "crated by weijia.deng");
            dbproperties.store(new FileOutputStream(webAppMainpath + "resources.local/"+ dbconfigFileName), "crated by weijia.deng");
            dbproperties.store(new FileOutputStream(webAppMainpath + "resources.beta/"+ dbconfigFileName), "crated by weijia.deng");
            dbproperties.store(new FileOutputStream(webAppMainpath + "resources.prod/"+ dbconfigFileName), "crated by weijia.deng");
            dbproperties.store(new FileOutputStream(webAppMainpath + "resources.dev/"+ dbconfigFileName), "crated by weijia.deng");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public void initialized(IntrospectedTable introspectedTable) {
        StringBuilder ddl = new StringBuilder();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection(introspectedTable.getContext().getJdbcConnectionConfiguration());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("show create table " + introspectedTable.getFullyQualifiedTableNameAtRuntime());
            while (rs.next()) {
                // ddl = rs.getString(2);
                ddl.append(rs.getString(2));
            }
            ddl.append(";");
            ddl.append(System.lineSeparator());
            ddl.append(System.lineSeparator());

            FileUtils.writeStringToFile(new File(this.dbsqlPath), ddl.toString(), "UTF-8", true);
        } catch (SQLException e) {
            throw new CodegenException("Failed to get ddl from database", e);
        } catch (IOException e) {
            throw new CodegenException("Failed to generate ddl for db.sql", e);
        }
    }


    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        this.testResourcePath = ConfigHolder.instance.getProperty("sys.webapp.testresourcepath");
        dbsqlPath = testResourcePath + "db.sql";
        ConfigHolder.instance.setProperty("sys.dbsqlPath", dbsqlPath);

        this.reourcepath = ConfigHolder.instance.getProperty("sys.webapp.resoucepath");
        ConfigHolder.instance.setProperty("sys.dbconfigpath", reourcepath + dbconfigFileName);

        username = ConfigHolder.instance.getProperty("db.userId");
        password = ConfigHolder.instance.getProperty("db.pwd");
        url = ConfigHolder.instance.getProperty("db.url");
        driver = ConfigHolder.instance.getProperty("db.driver");

        webAppMainpath = ConfigHolder.instance.getProperty("sys.webappRootPath")+"/src/main/";
        return true;
    }


}
