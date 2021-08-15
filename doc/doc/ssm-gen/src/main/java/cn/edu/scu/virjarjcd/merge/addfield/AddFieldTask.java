package cn.edu.scu.virjarjcd.merge.addfield;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;

import cn.edu.scu.virjarjcd.merge.MergeTask;
import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.XmlMapperMerger;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

/**
 * 增加一个字段
 * @author virjar
 *
 */
public class AddFieldTask extends MergeTask {

	private String dbSchema;
	private String dbTables;
	@Override
	public void run(Configuration configuration, String[] args) throws Exception {
		// TODO Auto-generated method stub
		Context context = configuration.getContexts().get(0);
		super.initParam(context);
		String modeAndField = args[2];
		String[] strings = modeAndField.split("\\.");
		if(strings.length != 2){
			System.err.println("useage: add field ModelName.FieldName JDBCTYPE");
			return;
		}
		String modeName = strings[0];
		String tableName ="";
		dbTables = ConfigHolder.instance.getProperty("db.tables");
		String[] alltableConfigurations = dbTables.split(",");
		for(String tableConfig:alltableConfigurations){
			if(tableConfig.toLowerCase().contains(modeName.toLowerCase())){
				tableName = tableConfig.split(":")[1];
				break;
			}
		}
		if(StringUtils.isEmpty(tableName)){
			System.err.println("cannot find defination for Model:"+modeName+",Please confirm it in config.properties file");
			return;
		}
		//now run introspections
		IntrospectedTable introspectedTable = introspectTable(this.buildTableConfiguation(modeName, tableName, context), context);


		//merge
		List<GeneratedJavaFile> mergeJavaFiles = new ArrayList<>();

		for(Plugin plugin:this.plugins){
			if(plugin instanceof MergePluginAdpter){
				MergePluginAdpter mergePlugin = (MergePluginAdpter) plugin;
				List<GeneratedJavaFile> mergeAdditionalJavaFiles = mergePlugin.mergeAdditionalJavaFiles(introspectedTable, args);
				if(mergeAdditionalJavaFiles != null){
					mergeJavaFiles.addAll(mergeAdditionalJavaFiles);
				}
			}
		}

		for(GeneratedJavaFile javaFile:mergeJavaFiles){
			super.writeFile(javaFile, javaFile.getFileEncoding());
		}

		//merge map xml file
		XmlMapperMerger xmlMapperMerger = new XmlMapperMerger();
		xmlMapperMerger.UNSerialization(introspectedTable);
		xmlMapperMerger.addField(introspectedTable, args);
		Document document = xmlMapperMerger.getDocument();
		GeneratedXmlFile gxf = new GeneratedXmlFile(document,
				introspectedTable.getMyBatis3XmlMapperFileName(), 
				introspectedTable.getMyBatis3XmlMapperPackage(),
				context.getSqlMapGeneratorConfiguration().getTargetProject(),
				true, context.getXmlFormatter());
		super.writeFile(gxf,"UTF-8");
	}

	protected TableConfiguration buildTableConfiguation(String ModelName,String talbeName,Context context){
		TableConfiguration tableConfiguration = new TableConfiguration(context);
		tableConfiguration.setSchema(dbSchema);
		tableConfiguration.setTableName(talbeName);
		tableConfiguration.setDomainObjectName(ModelName);
		tableConfiguration.setDeleteByExampleStatementEnabled(false);
		tableConfiguration.setSelectByExampleStatementEnabled(false);
		tableConfiguration.setCountByExampleStatementEnabled(false);
		tableConfiguration.setUpdateByExampleStatementEnabled(false);
		tableConfiguration.addProperty("ignoreQualifiersAtRuntime", "true");
		return tableConfiguration;
	}
}