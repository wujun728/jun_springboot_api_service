package com.gitee.freakchicken.dbapi.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @program: dolphinscheduler
 * @description:
 * @author: jiangqiang
 * @create: 2020-12-10 16:58
 **/
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final int TIMEOUT_MS = 30_000;

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(TIMEOUT_MS)
            .setSocketTimeout(TIMEOUT_MS)
            .setConnectionRequestTimeout(TIMEOUT_MS)
            .build();

    public static JSONObject post(String url, String map) {
        JSONObject result = new JSONObject();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            StringEntity entity = new StringEntity(map);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(REQUEST_CONFIG);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                result.put("success", true);
                result.put("data", responseString);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
            result.put("data", e.getMessage());
        }
        return result;
    }
}
