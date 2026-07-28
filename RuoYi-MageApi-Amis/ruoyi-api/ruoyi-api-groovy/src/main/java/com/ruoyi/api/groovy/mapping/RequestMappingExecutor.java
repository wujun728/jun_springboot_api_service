package com.ruoyi.api.groovy.mapping;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.api.groovy.service.GroovyApiService;
import com.ruoyi.api.groovy.entity.GroovyApiConfig;
import com.ruoyi.api.groovy.entity.ApiSql;
import com.ruoyi.api.groovy.entity.GroovyResult;
import com.ruoyi.api.groovy.interfaces.IGroovyExecutor;
import com.ruoyi.api.groovy.cache.GroovyApiConfigCache;
// TODO: Replace with RuoYi equivalents
//import com.ruoyi.api.groovy.rest.service.RestSqlService;
//import com.ruoyi.api.groovy.rest.util.HttpRequestUtil;
//import com.ruoyi.api.groovy.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import cn.hutool.extra.spring.SpringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestMappingExecutor implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private GroovyApiService groovyApiService;

	@Autowired
	private ServerProperties serverProperties;

	public void init(Boolean isStart) throws Exception {

	}

	private String getIpAndAdress() {
		String content = serverProperties.getServlet().getContextPath() == null ? ""
				: serverProperties.getServlet().getContextPath();
		Integer port = serverProperties.getPort() == null ? 8080 : serverProperties.getPort();
		String context = SpringUtil.getProperty("project.groovy-api.context");
		return "http://localhost:" + port + ("/" + content + context).replace("//", "/");
	}

	/**
	 * 执行脚本逻辑
	 */
	@RequestMapping
	@ResponseBody
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		Class<? extends RequestMappingExecutor> cls = this.getClass();
		try {
			Method method = cls.getDeclaredMethod("process", HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request, response);
		} catch (NoSuchMethodException e) {
			log.warn("当前子类未实现自定义方法[process]，走默认Bean定义的逻辑[无自定义执行逻辑]");
			try {
				RequestMappingExecutor executor = SpringUtil.getBean("HttpMappingExecutor");
				executor.execute(request, response);
			} catch (NoSuchBeanDefinitionException ex ) {
			} catch (RuntimeException  ex ) {
				log.warn(ex.getMessage());
			} catch (Exception  ex ) {
				log.warn(ex.getMessage());
			}
			log.info("执行默认方法的逻辑[无自定义执行逻辑]");
			defaultMetod(request, response);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SneakyThrows
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
		if (parent == null) {
			init(true);
		}
	}

    public static Map<String, Object> getSqlParam(HttpServletRequest request, GroovyApiConfig config) {
        Map<String, Object> map = new HashMap<>();

        JSONArray requestParams = JSON.parseArray(config.getParams());
        for (int i = 0; i < requestParams.size(); i++) {
            JSONObject jo = requestParams.getJSONObject(i);
            String name = jo.getString("name");
            String type = jo.getString("type");

            if (type.startsWith("Array")) {
                String[] values = request.getParameterValues(name);
                if (values != null) {
                    List<String> list = Arrays.asList(values);
                    if (values.length > 0) {
                        switch (type) {
                            case "Array<double>":
                                List<Double> collect = list.stream().map(value -> Double.valueOf(value)).collect(Collectors.toList());
                                map.put(name, collect);
                                break;
                            case "Array<bigint>":
                                List<Long> longs = list.stream().map(value -> Long.valueOf(value)).collect(Collectors.toList());
                                map.put(name, longs);
                                break;
                            case "Array<string>":
                            case "Array<date>":
                                map.put(name, list);
                                break;
                        }
                    } else {
                        map.put(name, list);
                    }
                } else {
                    map.put(name, null);
                }
            } else {

                String value = request.getParameter(name);
                if (StringUtils.isNotBlank(value)) {

                    switch (type) {
                        case "double":
                            Double v = Double.valueOf(value);
                            map.put(name, v);
                            break;
                        case "bigint":
                            Long longV = Long.valueOf(value);
                            map.put(name, longV);
                            break;
                        case "string":
                        case "date":
                            map.put(name, value);
                            break;
                    }
                } else {
                    map.put(name, value);
                }
            }
        }
        return map;
    }


	//*****************************************************************************************************************
	//*****************************************************************************************************************
	//*****************************************************************************************************************


	// TODO: Create RestSqlService equivalent in com.ruoyi.api.groovy or use RuoYi's service
	//@Resource
	//RestSqlService restSqlService;
	/**
	 * 执行脚本逻辑
	 */
	public void defaultMetod(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		log.info("servlet execute");

		log.info("HttpMappingExecutor execute  begin ");
		Object data = null;
		String servletPath = request.getRequestURI();
		PrintWriter out = null;
		try {
			GroovyApiConfig config = GroovyApiConfigCache.get(servletPath);
			if (config == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.setContentType(request.getContentType());
				response.setCharacterEncoding(CharsetUtil.UTF_8);
				out = response.getWriter();
				out.append(JSON.toJSONString(GroovyResult.fail("Api not exists")));
			}
			switch (config.getScriptType()) {
				case "SQL":
					ApiSql apiSql = new ApiSql();
					apiSql.setPath(config.getPath());
					apiSql.setText(config.getScriptContent());
					// TODO: Implement SQL execution or call RuoYi's service
					// Map<String, Object> parameters = HttpRequestUtil.getAllParameters(request);
					// data = restSqlService.doSQLProcess(apiSql, parameters);
					break;
				case "Class":
					data = doGroovyProcess(config, request, response);
					break;
				case "Groovy":
					data = doGroovyProcess(config, request, response);
					break;
				case "Jython":
					data = doGroovyProcess(config, request, response);
					break;
				case "JavaScript":
					data = doGroovyProcess(config, request, response);
				case "Jruby":
					data = doGroovyProcess(config, request, response);
					break;
				default:
					break;
			}
			response.setContentType(request.getContentType());
			response.setCharacterEncoding(CharsetUtil.UTF_8);
			out = response.getWriter();
			out.append(JSON.toJSONString(data));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType(request.getContentType());
			response.setCharacterEncoding(CharsetUtil.UTF_8);
			out = response.getWriter();
			out.append(JSON.toJSONString(GroovyResult.fail(e.toString())));
			log.error(e.toString(), e);
		} finally {
			if (out != null)
				out.close();
		}
		log.info("HttpMappingExecutor execute  end ");
	}


	public static void timeOut(String[] args) {
		long timeout = 3000;

		CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
			return new Object();
		});
		try {
			Object result = future.get(timeout, TimeUnit.MILLISECONDS);
			System.out.println("方法执行完毕，结果：" + result.toString());
		} catch (InterruptedException e) {
			System.out.println("出现异常，结束该方法的执行");
			future.cancel(true);
		} catch (ExecutionException e) {
			System.out.println("出现异常，结束该方法的执行");
			future.cancel(true);
		} catch (TimeoutException e) {
			System.out.println("超时了，结束该方法的执行");
			future.cancel(true);
		}
	}


	public Object doGroovyProcess(GroovyApiConfig config, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String beanName = GroovyApiConfigCache.getByPath(config.getPath());
		// TODO: Replace with RuoYi's request parameter extraction
		// Map<String, Object> params = HttpRequestUtil.getAllParameters(request);
		Map<String, Object> params = new HashMap<>();
		Object beanObj = SpringUtil.getBean(beanName);
		try {
			if(beanObj instanceof IGroovyExecutor){
				IGroovyExecutor bean = (IGroovyExecutor) beanObj;
				return bean.run(params);
			}
		// TODO: Handle BusinessException when ported
		// } catch (BusinessException e) {
		// 	return GroovyResult.fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "ERROR：执行错误，请检查执行日志并捕获并处理异常！";
	}



}