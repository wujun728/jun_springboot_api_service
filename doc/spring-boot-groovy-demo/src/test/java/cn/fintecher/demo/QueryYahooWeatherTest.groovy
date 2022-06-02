package cn.fintecher.demo

import groovy.json.JsonSlurper

/**
 * Created by ChenChang on 2018/6/13.
 */
class QueryYahooWeatherTest {
    static void main(String[] args) {
        URL apiUrl = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
        def response = new JsonSlurper().parse(apiUrl)

        println(response.getAt("query").getAt("results").getAt("channel").getAt("title"))
        println(response.query.results.channel.title)

    }

}
