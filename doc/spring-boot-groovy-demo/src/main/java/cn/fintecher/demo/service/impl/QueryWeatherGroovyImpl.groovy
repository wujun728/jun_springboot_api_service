package cn.fintecher.demo.service.impl

import cn.fintecher.demo.model.CommonRequest
import cn.fintecher.demo.model.CommonResponse
import cn.fintecher.demo.service.QueryThirdParty
import cn.fintecher.demo.service.QueryWeather
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * Created by ChenChang on 2018/6/13.
 */
@Service("queryWeatherGroovy")
class QueryWeatherGroovyImpl implements QueryWeather {
    @Autowired
    private QueryThirdParty testWeather
    @Autowired
    private QueryThirdParty yahooWeather

/**
 * 这里的逻辑和脚本中的一致 我们取了2个数据源 一个是yahoo的天气，一个是我们自己随便做的test天气
 * 将两个天气查询接口的返回封装成了我们需要的返回对象，并使用雅虎天气的返回为准
 * @param request
 * @return
 */
    @Override
    CommonResponse queryWeather(CommonRequest request) {
        cn.fintecher.demo.model.CommonResponse commonResponse = new cn.fintecher.demo.model.CommonResponse()
        def data = ["yahoo weather": yahooWeather.query(), "test weather": testWeather.query()]
        commonResponse.data = data
        commonResponse.responseMsg = "成功"
        commonResponse.bizResponseMsg = "成功"
        commonResponse.flag = data.get("yahoo weather").get("title")//使用yahoo天气的返回为准，我这里随便以解析了的title为例子
        return commonResponse
    }

    @Override
    Object queryWeather2(CommonRequest request) {
        def map =[:]
        def data = ["yahoo weather": yahooWeather.query(), "test weather": testWeather.query()]
        map.data = data
        map.responseMsg = "成功"
        map.bizResponseMsg = "成功"
        map.flag = data.get("yahoo weather").get("title")//使用yahoo天气的返回为准，我这里随便以解析了的title为例子
        return map
    }
}
