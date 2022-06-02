package cn.fintecher.demo.service.impl;

import cn.fintecher.demo.service.QueryThirdParty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChenChang on 2018/6/13.
 */
@Service("testWeather")
public class TestWeatherImpl implements QueryThirdParty {
    @Override
    public Map<String, Object> query(Map<String, Object> request) {
        Map<String,Object> map=new HashMap<>();
        map.put("title","test");
        return map;
    }
}
