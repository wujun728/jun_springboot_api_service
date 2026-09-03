package com.zapi.dataset.utils;

import java.util.UUID;

/**
 * JDK自带UUID
 * @author YangCan
 *
 */
public class UUIDGenerator {

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
