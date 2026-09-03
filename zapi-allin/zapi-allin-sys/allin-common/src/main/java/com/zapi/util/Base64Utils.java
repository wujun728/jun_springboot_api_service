package com.zapi.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;


public class Base64Utils {

	
	/***
	 * 解码
	 * @param str
	 * @return
	 * @throws IOException
	 */
	 public static byte[] decode(String str){
    	BASE64Decoder bd = new BASE64Decoder();
		byte[] bs = null;
		try {
			bs = bd.decodeBuffer(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bs;
    }

	/***
	 * 编码
	 * @param bytes
	 * @return
	 */
    public static String encode(byte[] bytes){
    	BASE64Encoder be = new BASE64Encoder();
		String encoder = be.encode(bytes);
		return encoder;
    }
  
	    
}
