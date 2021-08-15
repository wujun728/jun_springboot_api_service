package cn.edu.scu.virjarjcd.merge.addtable;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

import cn.edu.scu.virjarjcd.merge.MergeTask;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class AddTableTask extends MergeTask {

	private String dbSchema;
	@Override
	public void run(Configuration configuration, String[] args) {
		// TODO Auto-generated method stub
		dbSchema = ConfigHolder.instance.getProperty("db.schema");
		addTableConfigratons(configuration.getContexts().get(0), args[2]);
		List<String> warnings = new ArrayList<String>();
		DefaultShellCallback callback = new DefaultShellCallback("true".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.overided")));
		MyBatisGenerator myBatisGenerator;
		try {
			myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
			myBatisGenerator.generate(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String str:warnings){
			System.out.println(str);
		}
	}

	private void addTableConfigratons(Context context,String dbTables){
		String[] tables = dbTables.split(",");
		for(String table:tables){
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

}
