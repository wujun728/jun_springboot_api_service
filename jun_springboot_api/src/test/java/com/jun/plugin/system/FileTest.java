package com.jun.plugin.system;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class FileTest {

	public static void main(String[] args) throws IOException {
//		String filePath = "‪D:\\202207\\s1.txt";
		String filePath = "D:\\Documents\\Desktop\\1111.txt";
		
		List<String> lines = FileUtils.readLines(new File(filePath));
		List<String> linesNew = Lists.newArrayList();
		for(int i = 0; i < lines.size(); i ++) {
			String str = lines.get(i);
			String oldStr = str;
			if (!oldStr.contains("addWalter")  && oldStr.contains("upload") ) {
				str = str.substring(0, str.lastIndexOf(".")+1)+"mp4";
				str = str.substring(0, str.lastIndexOf("/")+1)+"addWalter"+str.substring(str.lastIndexOf("/")+1,str.length());
				str = str.replace("f2d.jieshundb.com", "ferddown.meilifangyy.cn");
				str = str.replace("jm.jieshundb.com", "ferddown.meilifangyy.cn");
				//System.out.println(oldStr);
				System.out.println();
				System.out.println(str);
				linesNew.add(str);
			}
			if(oldStr.contains("addWalter")) {
				str = str.substring(0, str.lastIndexOf(".")+1)+"mp4";
				//str = str.substring(0, str.lastIndexOf("/")+1)+""+str.substring(str.lastIndexOf("/")+1,str.length());
				str = str.replace("f2d.jieshundb.com", "ferddown.meilifangyy.cn");
				str = str.replace("jm.jieshundb.com", "ferddown.meilifangyy.cn");
				//System.out.println(oldStr);
				System.err.println();
				System.err.println(str);
				linesNew.add(str);
			}
		}
		System.err.println(linesNew.size());
		//lines.stream().forEach(System.err::println);
	}

}
