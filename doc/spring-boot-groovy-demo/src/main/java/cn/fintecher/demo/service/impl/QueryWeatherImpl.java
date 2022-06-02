package cn.fintecher.demo.service.impl;

import cn.fintecher.demo.model.CommonRequest;
import cn.fintecher.demo.model.CommonResponse;
import cn.fintecher.demo.service.QueryThirdParty;
import cn.fintecher.demo.service.QueryWeather;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ChenChang on 2018/6/13.
 */
@Service("queryWeather")
public class QueryWeatherImpl implements QueryWeather {
    @Autowired
    private QueryThirdParty testWeather;
    @Autowired
    private QueryThirdParty yahooWeather;


    /**
     * 这是使用脚本引擎的实现
     * @param request
     * @return
     */
    @Override
    public CommonResponse queryWeather(CommonRequest request) {
        Binding binding = new Binding();
        //进行上下文的传递 这里就是我有多少个第三方接口
        binding.setVariable("testWeather", testWeather);
        binding.setVariable("yahooWeather", yahooWeather);
        //这里直接用了 GroovyShell 当然也可以是runGroovyScript
        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate(getScript());
        CommonResponse response=new CommonResponse();
        BeanUtils.copyProperties(value,response);
        return  response;
    }

    @Override
    public Object queryWeather2(CommonRequest request) {
        Binding binding = new Binding();
        //进行上下文的传递 这里就是我有多少个第三方接口
        binding.setVariable("testWeather", testWeather);
        binding.setVariable("yahooWeather", yahooWeather);
        //这里直接用了 GroovyShell 当然也可以是runGroovyScript
        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate(getScript2());
        return value;
    }

    /**
     * 模拟了一个数据读取 获取了脚本 这里我们例子中查询天气2个接口 通过组织完成返回的组装
     * @return
     */
    private String getScript(){
     String script =" cn.fintecher.demo.model.CommonResponse commonResponse = new cn.fintecher.demo.model.CommonResponse()\n" +
             "        def data = [\"yahoo weather\": yahooWeather.query(), \"test weather\": testWeather.query()]\n" +
             "        commonResponse.data = data\n" +
             "        commonResponse.responseMsg = \"成功\"\n" +
             "        commonResponse.bizResponseMsg = \"成功\"\n" +
             "        commonResponse.flag=data.get(\"yahoo weather\").get(\"title\")\n" +
             "        return commonResponse\n"
             ;
     return script;
    }

    /**
     * 模拟了一个数据读取 获取了脚本 这里我们例子中查询天气2个接口 通过组织完成返回的组装
     * @return
     */
    private String getScript2(){
        String script ="     def map =[:]\n" +
                "        def data = [\"yahoo weather\": yahooWeather.query(), \"test weather\": testWeather.query()]\n" +
                "        map.data = data\n" +
                "        map.responseMsg = \"成功\"\n" +
                "        map.bizResponseMsg = \"成功\"\n" +
                "        map.flag = data.get(\"yahoo weather\").get(\"title\")//使用yahoo天气的返回为准，我这里随便以解析了的title为例子\n" +
                "        return map"
                ;
        return script;
    }
}
