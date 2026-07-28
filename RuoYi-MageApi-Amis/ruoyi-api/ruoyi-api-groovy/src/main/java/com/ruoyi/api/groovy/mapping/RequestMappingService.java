package com.ruoyi.api.groovy.mapping;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.api.groovy.entity.GroovyApiConfig;
import com.ruoyi.api.groovy.util.MappingRegisterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RequestMappingService implements InitializingBean {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * 获取已注册的API地址
	 */
	public List<GroovyApiConfig> getPathListForCode() {

		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		List<GroovyApiConfig> result = new ArrayList<>(map.size());
		for (RequestMappingInfo info : map.keySet()) {

			if (map.get(info).getMethod().getDeclaringClass() == RequestMappingExecutor.class) {
				continue;
			}

			String groupName = map.get(info).getBeanType().getSimpleName();
			String context = SpringUtil.getProperty("project.groovy-api.context");
			String servicename = SpringUtil.getProperty("project.groovy-api.servicename");
			for (String path : MappingRegisterUtil.getPatterns(info)) {
				if (path.indexOf(context) == 0 || path.equals("/error")) {
					continue;
				}

				Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
				if (methods.isEmpty()) {
					GroovyApiConfig apiInfo = new GroovyApiConfig();
					apiInfo.setPath(path);
					apiInfo.setMethod("All");
					apiInfo.setScriptType("Code");
					apiInfo.setBeanName(servicename);
					apiInfo.setCreator("admin");
					apiInfo.setDatasourceId("");
					apiInfo.setScriptContent("");
					apiInfo.setPath(path);
					result.add(apiInfo);
				} else {
					for (RequestMethod method : methods) {
						GroovyApiConfig apiInfo = new GroovyApiConfig();
						apiInfo.setPath(path);
						apiInfo.setMethod(method.name());
						apiInfo.setScriptType("Code");
						apiInfo.setBeanName(servicename);
						apiInfo.setCreator("admin");
						apiInfo.setDatasourceId("");
						apiInfo.setScriptContent("");
						apiInfo.setPath(path);
						result.add(apiInfo);
					}
				}

			}
		}
		return result;
	}
	


	/**
	 * 注册mapping
	 *
	 * @param apiInfo
	 */
	public synchronized void registerMappingForApiConfig(GroovyApiConfig apiInfo) throws NoSuchMethodException {
		 MappingRegisterUtil.registerMapping(apiInfo.getMethod(), apiInfo.getPath(), apiInfo.getScriptType());
	}


	/**
	 * 取消注册mapping
	 *
	 * @param apiInfo
	 */
	public synchronized void unregisterMappingForApiConfig(GroovyApiConfig apiInfo) {
		MappingRegisterUtil.unregisterMapping(apiInfo.getMethod(), apiInfo.getPath(), apiInfo.getScriptType());
	}


	/**
	 * 判断是否是原始代码注册的mapping
	 * 
	 * @param method
	 * @param pattern
	 */
	public Boolean isCodeMapping(String pattern, String method) {
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		for (RequestMappingInfo info : map.keySet()) {
			if (map.get(info).getMethod().getDeclaringClass() == RequestMappingExecutor.class) {
				continue;
			}
			Set<String> patterns = MappingRegisterUtil.getPatterns(info);
			Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
			if (patterns.contains(pattern) && (methods.isEmpty() || methods.contains(RequestMethod.valueOf(method)))) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info(" RequestMappingService is init .... ");
	}


}