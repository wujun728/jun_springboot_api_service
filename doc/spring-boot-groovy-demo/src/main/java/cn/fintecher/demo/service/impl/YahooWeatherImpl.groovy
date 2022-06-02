package cn.fintecher.demo.service.impl

import cn.fintecher.demo.service.QueryThirdParty
import groovy.json.JsonSlurper
import org.springframework.stereotype.Service

/**
 * 这里我懒得用JAVA实现了 这里用Groovy是一样的 这部分也可以考虑脚本化
 * 但是可能会存在每个接口都有不同的上下文和配置，脚本其实意义很弱了就
 * Created by ChenChang on 2018/6/13.
 */
@Service("yahooWeather")
class YahooWeatherImpl implements QueryThirdParty {
    @Override
    Map<String, Object> query(Map<String, Object> request) {

        URL apiUrl = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
        def response = new JsonSlurper().parse(apiUrl)
        def map = [title:response.query.results.channel.title]
        assert map instanceof HashMap
        return map
    }
}
