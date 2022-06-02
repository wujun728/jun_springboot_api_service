package cn.fintecher.demo.service;

import java.util.Map;

/**
 * Created by ChenChang on 2018/6/13.
 */
public interface QueryThirdParty {
    Map<String, Object> query(Map<String, Object> request);
}
