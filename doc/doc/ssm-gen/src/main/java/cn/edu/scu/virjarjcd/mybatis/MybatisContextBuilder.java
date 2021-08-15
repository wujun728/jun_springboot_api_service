package cn.edu.scu.virjarjcd.mybatis;

import cn.edu.scu.virjarjcd.codegertator.SysPluginCollector;
import cn.edu.scu.virjarjcd.exception.ValidationError;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.FileUtils;
import cn.edu.scu.virjarjcd.util.RegexUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.config.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

@Data
public class MybatisContextBuilder {

    //系统属性
    private String sysExportPath;
    private String sysProjectName;
    private String sysBasePackage;
    private String sysBuildTool;
    private Boolean sysLombok;
    private Boolean sysOverided;
    private Boolean sysMerged;
    private String moduleType;

    //数据库连接信息
    private String dbUrl;
    private String dbDriver;
    private String dbUserName;
    private String dbPassword;
    private String dbSchema;
    //tables 描述需要生成的表，以及对应的Model名称，格式如下
    //table1:Table1,table2:Table2 ->>数据库表名:Model名称
    private String dbTables;


    public Configuration parse(Properties properties, boolean ismerge) {
        this.sysExportPath = properties.getProperty("project.exportPath");
        this.sysProjectName = properties.getProperty("project.name");
        this.sysBasePackage = properties.getProperty("project.basePackage");
        this.sysBuildTool = properties.getProperty("project.buildTool");
        this.moduleType = properties.getProperty("project.moduleType");
        this.sysLombok = "true".equalsIgnoreCase(properties.getProperty("project.lombok"));
        this.sysOverided = "true".equalsIgnoreCase(properties.getProperty("project.overided"));
        this.sysMerged = "true".equalsIgnoreCase(properties.getProperty("project.merged"));


        this.dbUrl = properties.getProperty("db.url");
        this.dbDriver = properties.getProperty("db.driver");
        this.dbUserName = properties.getProperty("db.userId");
        this.dbPassword = properties.getProperty("db.pwd");
        this.dbSchema = properties.getProperty("db.schema");
        this.dbTables = properties.getProperty("db.tables");
        copy2Holder(properties, ConfigHolder.instance);
        return build(ismerge);
    }

    private void copy2Holder(Properties properties, ConfigHolder holder) {
        Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Object, Object> next = iterator.next();
            holder.put(next.getKey(), next.getValue());
        }
    }

    public Configuration build(boolean ismerge) {
        validate();
        writeToSystemConfig(ConfigHolder.instance);
        ConfigHolder.calculateData();
        return innerBuild(ismerge);
    }

    private Configuration innerBuild(boolean ismerge) {
        Configuration answer = new Configuration();
        Context context = new Context(null);
        answer.addContext(context);


        context.setId("mysql");
        if (!ConfigHolder.isSingleLayer()) {
            context.setTargetRuntime("cn.edu.scu.virjarjcd.codegertator.IntrospectedTableVirjarImpl");
        }

        if (ConfigHolder.instance.getProperty("formater.java", "mybatis").toLowerCase().equals("astparser")) {
            context.addProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER, "cn.edu.scu.virjarjcd.mybatis.formater.java.AstCodeParser");
        } else if (ConfigHolder.instance.getProperty("formater.java", "mybatis").toLowerCase().equals("jalopy")) {
            context.addProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER, "cn.edu.scu.virjarjcd.mybatis.formater.java.JalopyCodeParser");
        }

        addDeafaultPlugin(context, ismerge);
        addSysPlugin(context, ismerge);

        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
        commentGeneratorConfiguration.setConfigurationType("cn.edu.scu.virjarjcd.mybatis.CommentGenerator");
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        //jdbc
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(dbUrl);
        jdbcConnectionConfiguration.setDriverClass(dbDriver);
        jdbcConnectionConfiguration.setPassword(dbPassword);
        jdbcConnectionConfiguration.setUserId(dbUserName);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        //javaModelGenerator model->> entity
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage(ConfigHolder.instance.getProperty("sys.entitypackage"));
        if(ConfigHolder.isSingleLayer()){
            javaModelGeneratorConfiguration.setTargetProject(ConfigHolder.instance.getProperty("sys.serviceApi.javasourcepath"));
        }else {
            javaModelGeneratorConfiguration.setTargetProject(ConfigHolder.instance.getProperty("sys.service.javasourcepath"));
        }
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        //sqlMapGenerator
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage(ConfigHolder.instance.getProperty("sys.sqlmapperpackage"));
        sqlMapGeneratorConfiguration.setTargetProject(ConfigHolder.instance.getProperty("sys.sqlmapperpath"));
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        //javaClientGenerator
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(ConfigHolder.instance.getProperty("sys.mapperinterfacepackage"));
        javaClientGeneratorConfiguration.setTargetProject(ConfigHolder.instance.getProperty("sys.service.javasourcepath"));
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        addTableConfigratons(context, ismerge);
        return answer;
    }


    private void addTableConfigratons(Context context, boolean ismerge) {
        if (ismerge)//merge模式下，不对配置文件中的表生效
            return;
        String[] tables = dbTables.split(",");
        for (String table : tables) {
            String[] tableAndEntity = table.split(":");
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setSchema(dbSchema);
            tableConfiguration.setTableName(tableAndEntity[0]);
            tableConfiguration.setDomainObjectName(tableAndEntity[1]);
            tableConfiguration.setDeleteByExampleStatementEnabled(false);
            tableConfiguration.setSelectByExampleStatementEnabled(false);
            tableConfiguration.setCountByExampleStatementEnabled(false);
            tableConfiguration.setUpdateByExampleStatementEnabled(false);
            tableConfiguration.setConfiguredModelType("flat");
            tableConfiguration.addProperty("ignoreQualifiersAtRuntime", "true");
            context.addTableConfiguration(tableConfiguration);
        }
    }

    private void addSysPlugin(Context context, boolean ismerge) {
        List<PluginConfiguration> plugins = new SysPluginCollector().calcute().getplugin();
        for (PluginConfiguration pluginConfiguration : plugins) {
            context.addPluginConfiguration(pluginConfiguration);
        }
    }

    private void addDeafaultPlugin(Context context, boolean ismerge) {
        PluginConfiguration configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.mybatis.plugins.AddRepositoryAnnotationPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.mybatis.plugins.MySQLPagePlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.mybatis.plugins.RenameMapperClassPlugin");
        context.addPluginConfiguration(configuration);
        configuration.addProperty("searchString", "Mapper$");
        configuration.addProperty("replaceString", "Repository");

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.service.ServiceApiPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.controller.ControllerPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.JDBCPropertiesPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();//这个插件只会在merge的时候生效
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.model.ModelPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();//这个插件只会在merge的时候生效
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.entity.EntityMergePlugin");
        context.addPluginConfiguration(configuration);


        //if(!ismerge){//merge模式下，如下插件不调用,以修改，在插件validate的时候会自动根据全局mergeflag决定是否调用插件
        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.utils.beanmapper.BeanMapperUtilPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.utils.exception.ProjectExceptionPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.utils.rest.RestWraperPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.utils.utils.UtilsPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.WebInfoPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.ApplicationContextPlugin");
        context.addPluginConfiguration(configuration);


        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.MVCServletePlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.buildtool.maven.POMPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.GitignorePlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.logback.XmlLogbackPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.configfile.MybayisConfigPlugin");
        context.addPluginConfiguration(configuration);

        configuration = new PluginConfiguration();
        configuration.setConfigurationType("cn.edu.scu.virjarjcd.feature.vo.NoNullIncludePlugin");
        context.addPluginConfiguration(configuration);

        //}
    }

    private void writeToSystemConfig(ConfigHolder holder) {
        holder.setProperty("sys.exportPath", sysExportPath);
        holder.setProperty("sys.projectName", sysProjectName);
        holder.setProperty("sys.basePackage", sysBasePackage);
        holder.setProperty("sys.buildTool", sysBuildTool);
        holder.setProperty("sys.lombok", String.valueOf(sysLombok));

        holder.setProperty("db.url", this.dbUrl);
        holder.setProperty("db.driver", this.dbDriver);
        holder.setProperty("db.userId", this.dbUserName);
        holder.setProperty("db.pwd", this.dbPassword);
        holder.setProperty("db.schema", this.dbSchema);
        holder.setProperty("db.tables", this.dbTables);
    }

    private void validate() {
        if (StringUtils.isEmpty(sysBuildTool)) {
            sysBuildTool = "maven";
        }
        if (sysLombok == null) sysLombok = true;
        if (sysOverided == null) sysOverided = true;
        if (sysMerged == null) sysMerged = false;
        if (StringUtils.isEmpty(sysProjectName)) {
            sysProjectName = "virjarjcd";
        }
        if (StringUtils.isEmpty(sysExportPath)) {
            throw new ValidationError("导出路径不存在");
        }
        if (!FileUtils.createIfNotExist(sysExportPath + File.separator + sysProjectName + File.separator)) {
            throw new ValidationError("不能在配置的目录写入数据");
        }
        if (!sysOverided && (!ConfigHolder.isIsmerge())) {
            try {
                FileUtils.cleanDirectory(new File(sysExportPath + File.separator + sysProjectName + File.separator));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ValidationError("工程清理失败");
            }
        }
        if (StringUtils.isEmpty(sysBasePackage)) {
            sysBasePackage = "com.virjar";
        }

        if (StringUtils.isEmpty(dbDriver)) {
            //throw new ValidationError("未配置");
            dbDriver = "com.mysql.jdbc.Driver";
        }
        if (StringUtils.isEmpty(dbPassword)) {
            throw new ValidationError("未配置数据库密码");
        }
        if (StringUtils.isEmpty(dbUrl)) {
            throw new ValidationError("未配置数据库URL");
        }
        if (!dbUrl.matches("jdbc:mysql://\\S+:\\d{2,5}/\\S+")) {//jdbc:mysql://localhost:3306/meiweidi
            throw new ValidationError("数据库URL格式不合法");
        }
        if (StringUtils.isEmpty(dbSchema)) {
            dbSchema = RegexUtil.extract(dbUrl, "jdbc:mysql://\\S+:\\d{2,5}/(\\w+)(\\?.+)?", 1);
        }
        if (StringUtils.isEmpty(dbTables)) {
            throw new ValidationError("未配置导出表");
        }
        if (!dbTables.matches("\\w+:\\w+(,\\w+:\\w+)*")) {
            throw new ValidationError("导出表格式不合法");
        }
        if (StringUtils.isEmpty(dbUserName)) {
            throw new ValidationError("未配置数据账户");
        }
    }
}
