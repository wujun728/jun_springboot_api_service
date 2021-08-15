package cn.edu.scu.virjarjcd.merge;

import java.io.FileInputStream;
import java.util.Properties;

import org.mybatis.generator.config.Configuration;

import cn.edu.scu.virjarjcd.merge.addfield.AddFieldTask;
import cn.edu.scu.virjarjcd.merge.addtable.AddTableTask;
import cn.edu.scu.virjarjcd.merge.deletefield.DeleteFieldTask;
import cn.edu.scu.virjarjcd.merge.deletetable.DeleteTableTask;
import cn.edu.scu.virjarjcd.mybatis.MybatisContextBuilder;

/**
 * merge engine 代码混合引擎，解决实际开发过程中因需求变动引起的模型改变，
 * 常见场景有，增加表，修改表，删除表
 * <br/>
 * merge仍然需要链接数据库，请保持config文件正确
 * @author virjar
 */
public class MergeEngine {

	private String [] args;
	public static void main(String args[]){
		args = new String[]{"mergeEngine","add","field","Data.test","VARCHAR"};
		new MergeEngine(args).execute();
	}
	public MergeEngine(String[] args) {
		super();
		this.args = args;
	}

	public synchronized void execute(){
		if(args.length <3){
			System.err.println("useage:add table EntityName:tableName");
			System.err.println("\tadd field EntityName.filedName jdbcType");
			System.err.println("\tremove table EntityName");
			System.err.println("\tremove field EntityName:filedName");
			return;
		}
		try {
			Properties config = new Properties();
			config.load(new FileInputStream("config.properties"));
			Configuration configuration = new MybatisContextBuilder().parse(config,true);
			if(args[0].toLowerCase().equals("add")){
				if(args[1].toLowerCase().equals("table")){
					new AddTableTask().run(configuration, args);
				}else if(args[1].toLowerCase().equals("field")){
					new AddFieldTask().run(configuration, args);
				}else{
					System.err.println("can not recognize parameter "+args[1]);
				}
			}else if(args[0].toLowerCase().equals("remove")){
				if(args[1].toLowerCase().equals("table")){
					new DeleteTableTask().run(configuration, args);
				}else if(args[1].toLowerCase().equals("field")){
					System.err.println("can not recogernize parameter "+args[1]);
				}	new DeleteFieldTask().run(configuration, args);
			}else{
				System.err.println("can not recogernize parameter "+args[0]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
