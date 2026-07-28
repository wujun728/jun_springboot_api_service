package com.ruoyi.api.groovy.service;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.fastjson2.JSON;
// TODO: Replace with RuoYi's own DB utility
//import io.github.wujun728.db.record.Db;
//import io.github.wujun728.db.record.Record;
//import io.github.wujun728.db.record.Page;
//import io.github.wujun728.db.utils.RecordUtil;
import com.google.common.collect.Lists;
import com.ruoyi.api.groovy.entity.GroovyApiConfig;
import com.ruoyi.api.groovy.entity.ApiDataSource;
import com.ruoyi.api.groovy.entity.ApiSql;
import com.ruoyi.api.groovy.entity.GroovyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GroovyApiService {

	public void init(){
        StaticLog.info("GroovyApiService is staring ... ");
	}

	@SuppressWarnings("unchecked")
	public List<GroovyApiConfig> queryApiConfigList() {
		// TODO: Replace DB usage with RuoYi's own data access
		// List<Record> lists = Db.use("main").find("select * from api_config where status = 'ENABLE' ");
		List<GroovyApiConfig> datas = null;
		// List<GroovyApiConfig> datas = RecordUtil.recordToBeans(lists,GroovyApiConfig.class);
		if(!CollectionUtils.isEmpty(datas)) {
			buildApiConfigSubApiSql(datas);
		}
		if(Boolean.TRUE.equals(Boolean.valueOf(SpringUtil.getProperty("project.runApi.enable")))){
			queryCountSql();
			queryDatasourceList();
			querySQLList("0");
			getDatasource("0");
		}
		return datas;
	}


	public Integer queryCountSql() {
		// TODO: Replace DB usage
		// Integer count = Db.use("main").queryForInt("select count(*) from api_config");
		// return count;
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<ApiDataSource> queryDatasourceList() {
		// TODO: Replace DB usage
		// Page<Record> lists = Db.use("main").paginate(1,2,"select * "," from api_datasource where id <> ? ");
		// Console.log(JSON.toJSONString(RecordUtil.pageRecordToPageMap(lists)));
		// String from = "from api_datasource where id > ?";
		// String totalRowSql = "select count(*) " + from;
		// String findSql = "select * " + from + " order by id";
		// Db.paginate(1, 10, totalRowSql, findSql);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySQLList(String apiId) {
		// TODO: Replace DB usage
		// List<Record> lists = Db.use("main").find("select * from api_config");
		// List<ApiSql> datas = RecordUtil.recordToBeans(lists,ApiSql.class);
		// List<Map<String, Object>> datas2 = RecordUtil.recordToMaps(lists);
		// return datas2;
		return null;
	}
	

	@SuppressWarnings("unchecked")
	public ApiDataSource getDatasource(String id) {
		// TODO: Replace DB usage
		// ApiDataSource info = new ApiDataSource();
		// Record record = Db.use("main").findById("api_datasource", id);
		// return RecordUtil.recordToBean(record,ApiDataSource.class);
		return null;
	}


	private static void buildApiConfigSubApiSql(List<GroovyApiConfig> datas) {
		datas.stream().map(item->{
					List<ApiSql> sqlList = Lists.newArrayList();
					if("sql".equalsIgnoreCase(item.getScriptType())) {
						String sqls[] = item.getScriptContent().split(";");
						if(sqls.length>0) {
							for(String sql : sqls) {
								if(StringUtils.isEmpty(sql)) {
									continue;
								}
								ApiSql apisql = new ApiSql();
								apisql.setId(String.valueOf(item.getId()));
								apisql.setText(sql);
								sqlList.add(apisql);
							}
						}
					}
					item.setSqlList(sqlList);

					return item;
				}
		).collect(Collectors.toList());
	}
	

}