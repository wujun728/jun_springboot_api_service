package cn.fintecher.demo.web;

import cn.fintecher.demo.model.CommonRequest;
import cn.fintecher.demo.model.CommonResponse;
import cn.fintecher.demo.service.QueryWeather;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ChenChang on 2018/6/13.
 */
@RestController
@RequestMapping("/api/weather")
@Api(value = "天气查询接口", description = "天气查询接口")
public class WeatherController {
    @Autowired
    QueryWeather queryWeather;
    @Autowired
    QueryWeather queryWeatherGroovy;

    @GetMapping("/queryScript")
    @ApiOperation(value = "查询天气", notes = "查询天气 通过queryWeather")
    public ResponseEntity<CommonResponse> queryScript(CommonRequest request) {

        return ResponseEntity.ok().body(queryWeather.queryWeather(request));
    }

    @GetMapping("/queryScript2")
    @ApiOperation(value = "查询天气 通过queryWeather 由脚本构造返回", notes = "查询天气 通过queryWeather")
    public ResponseEntity<?> queryScript2(CommonRequest request) {

        return ResponseEntity.ok().body(queryWeather.queryWeather2(request));
    }
    @GetMapping("/queryGroovy")
    @ApiOperation(value = "查询天气", notes = "查询天气 通过queryWeatherGroovy")
    public ResponseEntity<CommonResponse> queryGroovy(CommonRequest request) {

        return ResponseEntity.ok().body(queryWeatherGroovy.queryWeather(request));
    }
    @GetMapping("/queryGroovy2")
    @ApiOperation(value = "查询天气 通过queryWeatherGroovy 由脚本构造返回", notes = "查询天气 通过queryWeatherGroovy")
    public ResponseEntity<?> queryGroovy2(CommonRequest request) {

        return ResponseEntity.ok().body(queryWeatherGroovy.queryWeather2(request));
    }
}
