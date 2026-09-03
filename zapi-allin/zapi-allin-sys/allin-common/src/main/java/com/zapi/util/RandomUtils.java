package com.zapi.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/***
 * 随机数工具类
 * @author zhonglz
 *
 */
public class RandomUtils {
	
	
	private static Random randGen = new Random();
	
	/***
	 * 随机生成一串随机数（数字）
	 * @param length  数字长度
	 * @return
	 */
	public static final String randomNumber(int length){
		char[] numbersAndLetters = ("1234567890").toCharArray();
		char[] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++){
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}
	
	/****
	 * 随机生成一串随机数（字母）
	 * @param length
	 * @return
	 */
	public static final String randomString(int length)
	{
		char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz" +		                   
				 "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		char[] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++){
			randBuffer[i] = numbersAndLetters[randGen.nextInt(52)];
		}
		return new String(randBuffer);
		
	}
	
	/****
	 * 随机生成一串随机数（字母+数字）
	 * @param length
	 * @return
	 */
	public static final String randomByStrOrNum(int length)
	{
		char[] numbersAndLetters = ("1234567890abcdefghijklmnopqrstuvwxyz" +		                   
				 "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		char[] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++){
			randBuffer[i] = numbersAndLetters[randGen.nextInt(72)];
		}
		return new String(randBuffer);
		
	}

	/****
	 * 随机生成6位的随机数
	 * 可修改为小写字母加数字的
	 * @return
	 */
	public static String random(int length){
		String Num="";
		//String base="abcdefghijklmnopqrstuvwxyz0123456789";
		String base="0123456789";
		Random random=new Random();
		for(int i=0;i<length;i++){
			int number=random.nextInt(base.length());
			char n=base.charAt(number);
			if (Num.indexOf(n) != -1) {
				if(i != 0){
					i--;
				}
			}else{
				Num += n +"";
			}
		}
		return Num.trim();
	}
	
	/**
	  * getRandomUnmber 获得大于0 小于1的随机数
	  * @author lxm
	  * @date  2019年12月13日
	 */
	public static BigDecimal getRandomUnmber() {
		Random random=new Random();
		 double d = random.nextDouble();//生成0-1的随机数  包含0.0 1.0
		 d = (double) Math.round(d * 100) / 100; //取小数点后两位
		 if(d==0.0) {
			 d=d+0.01;
		 }else if(d==1.0) {
			d=d-0.1;
		 }
		 BigDecimal decimalD=BigDecimal.valueOf(d);//将double类型转换为BigDecimal
		 return decimalD;
	}

	/*
	 * 印章编号
	 */
	public static String getSealNo(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String strName=format.format(new Date());
		return "YZBH_"+strName+random(6);
	}
	
	/***
	 * 生成订单号
	 * @return
	 */
	public static String getOrderNo(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		String strName=format.format(new Date());
		return "XXQ"+strName+random(6);
	}
	
	/***
	 * 生成订单交互号（用于微信支付）
	 * @return
	 */
	public static String getOrderJoinNo(){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		String strName=format.format(new Date());
		return "XXQJH"+strName+random(6);
	}
	
	public static void main(String[] args) {
	}
}
