package cn.edu.scu.virjarjcd.feature.configfile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.mybatis.generator.api.GeneratedJavaFile;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.util.ConfigHolder;

public class GitignorePlugin extends MergePluginAdpter {

	private String projectRootPath;
	
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		// TODO Auto-generated method stub
		try {
			FileUtils.copyInputStreamToFile(GitignorePlugin.class.getResourceAsStream("gitignore"), new File(new File(projectRootPath), ".gitignore"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean calcEnv() {
		// TODO Auto-generated method stub
		projectRootPath = ConfigHolder.instance.getProperty("sys.projectRootPath");
		return true;
	}

	
}
