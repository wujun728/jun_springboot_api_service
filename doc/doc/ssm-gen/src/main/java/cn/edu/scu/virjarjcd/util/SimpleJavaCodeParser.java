package cn.edu.scu.virjarjcd.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * java代码解析器，针对于mybatis codegerator的代码解析，不会检查java的所有语法，
 * 根据 org.mybatis.generator.api.dom.java  作为解析粒度
 * @author 邓维佳
 * @see org.mybatis.generator.api.dom.java 
 * @deprecated
 * 
 */
public class SimpleJavaCodeParser {

	private String packageStr;
	private List<String> importStrs = new ArrayList<String>();
	private String classComment;
	
	private byte[] buffer = new byte[1024];
	private byte[] pushbackbuffer = new byte[2048];
	private byte[] block = new byte[2018];
	private int pushbackoffet =0;
	private int offset =0;
	private int bufferlen =0;
	private InputStream is =null;
	private int keywordoff =0;
	
	public static void main(String args[]){
		Long date = 1457429401000l;
	SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	System.out.println(formater.format(date));
	}
	public  TopLevelClass parse(InputStream is) throws IOException{
		this.is = is;
		//TopLevelClass answer = new TopLevelClass(type);
		packageStr = parsePackage();
		
		return null;
	}
	private String parsePackage() throws IOException {
		// TODO Auto-generated method stub
		int blockoffset =0;
		int status =0;
		int ch = nextbyte();
		while(status !=1){
			block[blockoffset++] = (byte) ch;
			switch(status){
			case 0://初始状态
				if(ch == -1) return null;
				if(isEmpty(ch)){
					//donothing
				}else{
					status = 2;
					int match = matchkeyword("package",ch);
					if( match== -1){
						status = -1;
						return null;
					}else if(match == 1){
						status =3;
					}
				}
			case 2://匹配package
				int match = matchkeyword("package",ch);
				if( match== -1){
					status = -1;
					return null;
				}else if(match == 1){
					status =3;
				}
			case 3://匹配空行
				if(isEmpty(ch)){
					status = 4;
				}else{
					return null;
				}
			case 4:
				if(!isEmpty(ch)){
					status = 5;
					pushback(ch);
				}
			case 5:
				
			}
		}
		return null;
	}
	
	private  int matchkeyword(String keyword,int byt){
		if(keywordoff == keyword.length()-1) return 1;
		keywordoff++;
		if(keyword.charAt(keywordoff)== byt){
			return 0;
		}else{
			return -1;
		}
	}
	
	private static boolean isEmpty(int byt){
		return byt==' ' || byt=='\r' || byt=='\n' || byt=='\t';
	}
	
	private int nextbyte() throws IOException{
		if(pushbackoffet != 0){
			pushbackoffet--;
			return pushbackbuffer[pushbackoffet +1];
		}
		if(offset >= bufferlen){
			bufferlen = is.read(buffer, 0, 1024);
			offset = 0;
			if(bufferlen == 0){
				return -1;
			}
		}
		return buffer[offset++];
	}
	
	private void pushback(int byt){
		if(pushbackoffet >= 2047){
			throw new RuntimeException("只允许回退2047之内的字符数目");
		}
		pushbackoffet++;
		pushbackbuffer[pushbackoffet] = (byte)byt;
	}
	
	private void close(){
		try {
			this.is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
