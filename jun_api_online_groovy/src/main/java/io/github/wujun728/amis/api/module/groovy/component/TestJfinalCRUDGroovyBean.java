package io.github.wujun728.amis.api.module.groovy.component;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.github.wujun728.common.base.Result;
import io.github.wujun728.common.base.interfaces.IExecutor;
import io.github.wujun728.db.record.Db;
import io.github.wujun728.db.record.Record;
import io.github.wujun728.db.utils.DataSourcePool;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 需要把该代码放进DB，api_groovy，测试Jfinal-CURD-保存在庫裡面
 */
public class TestJfinalCRUDGroovyBean  implements IExecutor<Result, Map<String,Object>> {

	@PostConstruct
	public void test(){
		DataSourcePool.init("oa", SpringUtil.getProperty("spring.datasource.url"),
				SpringUtil.getProperty("spring.datasource.username"),
				SpringUtil.getProperty("spring.datasource.password"));
		Db.init(DataSourcePool.get("oa"));
	}



	@Override
	public Result execute(Map<String, Object> parms) throws Exception {
		Record data = new Record().set("title", "1111").set("content", "content").set("remark", "remark");
		Boolean resutl = Db.use("test1").save("test", data);
		Map<String, Object> params = parms;
		Map<String, Object> map = new HashMap<>();
		map.put("reward", "6666");
		map.put("resutl", resutl);
		System.out.println("返回数据为：" + JSONUtil.toJsonStr(map));
		return Result.success(map);
	}

}

