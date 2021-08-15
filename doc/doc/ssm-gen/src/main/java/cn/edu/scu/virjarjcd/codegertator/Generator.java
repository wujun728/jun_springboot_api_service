package cn.edu.scu.virjarjcd.codegertator;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import cn.edu.scu.virjarjcd.merge.MergeEngine;
import cn.edu.scu.virjarjcd.mybatis.MybatisContextBuilder;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class Generator {

	private boolean gen(Properties properties){
		try {
			Configuration configuration = new MybatisContextBuilder().parse(properties, false);
			List<String> warnings = new ArrayList<String>();
			DefaultShellCallback callback = new DefaultShellCallback("true".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.overided")));
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
			myBatisGenerator.generate(null);
			for (String str : warnings) {
				System.out.println(str);
			}
		}catch (Exception e){
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length>1){
			ConfigHolder.setIsmerge(true);
			new MergeEngine(args).execute();
			System.out.println("merge sucess");
		}else{
			gen();
			System.out.println("gen sucess");
		}
	}

	public static void gen() throws InvalidConfigurationException, SQLException, IOException, InterruptedException{
		Properties config = new Properties();
		config.load(new FileInputStream("config.properties"));
		Configuration configuration = new MybatisContextBuilder().parse(config,false);
		List<String> warnings = new ArrayList<String>();
		DefaultShellCallback callback = new DefaultShellCallback("true".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.overided")));
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
		myBatisGenerator.generate(null);
		for(String str:warnings){
			System.out.println(str);
		}
	}
}
