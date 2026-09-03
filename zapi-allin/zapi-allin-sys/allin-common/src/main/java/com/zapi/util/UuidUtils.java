package com.zapi.util;

import java.util.UUID;

/****
 * uuid
 * @author xiaozhong
 *
 */
public class UuidUtils {

	/**
	 * 获取UUID
	 * @return 不带-分隔符的32位UUID
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 获取UUID
	 * @return 带-分隔符的32位UUID
	 */
	public static String uuidToHG() {
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
	}
}
