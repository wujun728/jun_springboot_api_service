package cn.fintecher.demo.service;

import cn.fintecher.demo.model.CommonRequest;
import cn.fintecher.demo.model.CommonResponse;

/**
 * Created by ChenChang on 2018/6/13.
 */
public interface QueryWeather {
    /**
     * 统一返回的
     * @param request
     * @return
     */
    CommonResponse queryWeather(CommonRequest request);
    /**
     * 非统一返回
     * @param request
     * @return
     */
    Object queryWeather2(CommonRequest request);
}
