package com.gameloft9.demo.controllers;

import com.gameloft9.demo.beans.ApiBean;
import com.gameloft9.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 动态api处理controller，
 * 目前支持10个api注册，可手动添加
 * todo // 方法能动态生成就好了，就不用手动添加了。
 * Created by gameloft9 on 2018/8/9.
 */
@Controller
@Slf4j
public class ApiController {
    @ResponseBody
    public String index1() {
        ApiBean apiBean = Constants.requestMappings.get("1");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index2() {
        ApiBean apiBean = Constants.requestMappings.get("2");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index3() {
        ApiBean apiBean = Constants.requestMappings.get("3");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index4() {
        ApiBean apiBean = Constants.requestMappings.get("4");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index5() {
        ApiBean apiBean = Constants.requestMappings.get("5");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index6() {
        ApiBean apiBean = Constants.requestMappings.get("6");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index7() {
        ApiBean apiBean = Constants.requestMappings.get("7");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index8() {
        ApiBean apiBean = Constants.requestMappings.get("8");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index9() {
        ApiBean apiBean = Constants.requestMappings.get("9");
        return getReturnMsg(apiBean.getMsg());
    }

    @ResponseBody
    public String index10() {
        ApiBean apiBean = Constants.requestMappings.get("10");
        return getReturnMsg(apiBean.getMsg());
    }

    /**
     * 拿到注册的返回报文
     * */
    private String getReturnMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return "success";
        }

        return msg;
    }
}
