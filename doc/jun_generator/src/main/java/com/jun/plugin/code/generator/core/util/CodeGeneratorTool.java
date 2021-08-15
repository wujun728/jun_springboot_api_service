package com.jun.plugin.code.generator.core.util;


import java.io.IOException;

import com.jun.plugin.code.generator.core.model.ClassInfo;
import com.jun.plugin.code.meta.util.Table;

public class CodeGeneratorTool {

	/**
	 * process Table Into ClassInfo
	 *
	 * @param tableSql
	 * @return
	 */
	public static ClassInfo processTableIntoClassInfo(String tableSql) throws IOException {
		return TableParseUtil.processTableIntoClassInfo(tableSql);
	}
	
	public static Table processTableIntoTable(String tableSql) throws IOException {
		return TableParseUtil.processTableIntoTable(tableSql);
	}

}