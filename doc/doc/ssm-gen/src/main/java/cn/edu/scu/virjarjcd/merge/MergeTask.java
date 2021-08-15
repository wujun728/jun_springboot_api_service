package cn.edu.scu.virjarjcd.merge;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.db.DatabaseIntrospector;

public abstract class MergeTask {

	protected List<Plugin> plugins = new ArrayList<Plugin>();
	/**
	 * 参数起始偏移 3（数组下标号码）
	 * @throws Exception 
	 */
	public abstract void run(Configuration configuration,String [] args) throws Exception;

	protected void initParam(Context context){
		try {
			Field field = Context.class.getDeclaredField("pluginConfigurations");
			field.setAccessible(true);
			List<String> warnings = new ArrayList<String>();
			List<PluginConfiguration> pluginConfigurations = (List<PluginConfiguration>) field.get(context);
			for (PluginConfiguration pluginConfiguration : pluginConfigurations) {
				Plugin plugin = ObjectFactory.createPlugin(context,
						pluginConfiguration);
				if(plugin.validate(warnings)){
					plugins.add(plugin);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	protected IntrospectedTable introspectTable(TableConfiguration tc,Context context) throws SQLException{
		Connection connection = ConnectionFactory.getInstance().getConnection(
				context.getJdbcConnectionConfiguration());
		List<String> warnings = new ArrayList<String>();
		JavaTypeResolver javaTypeResolver = ObjectFactory
				.createJavaTypeResolver(context, warnings );
		DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(
				context, connection.getMetaData(), javaTypeResolver, warnings);
		List<IntrospectedTable> tables = databaseIntrospector
				.introspectTables(tc);
		connection.close();
		if(tables.size() >0){
			return tables.get(0);
		}
		return null;
	}

	/**
	 * Writes, or overwrites, the contents of the specified file
	 * 
	 * @param file
	 * @param content
	 * @throws ShellException 
	 */
	protected void writeFile(GeneratedJavaFile gjf, String fileEncoding) throws IOException, ShellException {
		File directory = getDirectory(gjf
				.getTargetProject(), gjf.getTargetPackage());


		File targetFile = new File(directory, gjf.getFileName());
		 
		FileOutputStream fos = new FileOutputStream(targetFile, false);
		OutputStreamWriter osw;
		if (fileEncoding == null) {
			osw = new OutputStreamWriter(fos);
		} else {
			osw = new OutputStreamWriter(fos, fileEncoding);
		}

		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(gjf.getFormattedContent());
		bw.close();
	}

	/**
	 * Writes, or overwrites, the contents of the specified file
	 * 
	 * @param file
	 * @param content
	 * @throws ShellException 
	 */
	protected void writeFile(GeneratedXmlFile gxf, String fileEncoding) throws IOException, ShellException {
		File directory = getDirectory(gxf
				.getTargetProject(), gxf.getTargetPackage());


		File targetFile = new File(directory, gxf.getFileName());
		 
		FileOutputStream fos = new FileOutputStream(targetFile, false);
		OutputStreamWriter osw;
		if (fileEncoding == null) {
			osw = new OutputStreamWriter(fos);
		} else {
			osw = new OutputStreamWriter(fos, fileEncoding);
		}

		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(gxf.getFormattedContent());
		bw.close();
	}
	
	public File getDirectory(String targetProject, String targetPackage)
			throws ShellException {
		// targetProject is interpreted as a directory that must exist
		//
		// targetPackage is interpreted as a sub directory, but in package
		// format (with dots instead of slashes). The sub directory will be
		// created
		// if it does not already exist

		File project = new File(targetProject);
		if (!project.isDirectory()) {
			throw new ShellException(getString("Warning.9", //$NON-NLS-1$
					targetProject));
		}

		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			sb.append(File.separatorChar);
		}

		File directory = new File(project, sb.toString());
		if (!directory.isDirectory()) {
			boolean rc = directory.mkdirs();
			if (!rc) {
				throw new ShellException(getString("Warning.10", //$NON-NLS-1$
						directory.getAbsolutePath()));
			}
		}

		return directory;
	}
}
