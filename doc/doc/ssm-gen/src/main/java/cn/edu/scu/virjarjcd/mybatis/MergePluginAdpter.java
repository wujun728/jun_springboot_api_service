package cn.edu.scu.virjarjcd.mybatis;

import java.util.List;

import cn.edu.scu.virjarjcd.feature.lombok.LombokGenPlugin;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import cn.edu.scu.virjarjcd.util.ConfigHolder;

public abstract class MergePluginAdpter extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		if(!calcEnv()){
			return false;
		}
		if(this instanceof LombokGenPlugin){
			return true;//bug，临时通过这个方案解决
		}
		return !ConfigHolder.isIsmerge();
	}

	public abstract boolean calcEnv();

	/**
	 * 构造java dom对象，根据新的表结构构造新的dom对象，merge engine会删除原先对象，使用新的dom数据覆盖
	 * @param introspectedTable
	 * @return
	 */
	public List<GeneratedJavaFile>  mergeAdditionalJavaFiles(IntrospectedTable introspectedTable,String args[]){
		return null;
	}
	
}
