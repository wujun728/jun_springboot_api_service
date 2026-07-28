package com.ruoyi.api.groovy.interfaces;

import java.util.Map;

public interface IGroovyExecutor<T, P extends Map> {
	
	/**
	 * 执行接口
	 * 
	 * @return
	 * @throws Exception 
	 */
	public T run(P params) throws Exception;

}