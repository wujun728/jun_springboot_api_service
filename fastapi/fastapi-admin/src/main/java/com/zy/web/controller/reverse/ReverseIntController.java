package com.zy.web.controller.reverse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.zy.common.core.domain.AjaxResult;
import com.zy.common.core.domain.model.LoginUser;
import com.zy.common.exception.ServiceException;
import com.zy.common.utils.DateUtils;
import com.zy.common.utils.SecurityUtils;
import com.zy.common.utils.StringUtils;
import com.zy.iservice.domain.IntfaceAppApplyFor;
import com.zy.iservice.domain.IntfaceReverseHistory;
import com.zy.iservice.service.IntfaceAppApplyForService;
import com.zy.reverse.service.IReverseIntService;
import com.zy.system.service.ISysConfigService;
import com.zy.common.utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author javachen
 * @description ReverseIntController
 */
@RestController
@CrossOrigin
@RequestMapping("/reverse")
public class ReverseIntController {

    @Autowired
    private IReverseIntService reverseIntService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IntfaceAppApplyForService intfaceAppApplyForService;

    /**
     * 执行get请求
     * @return
     */
    @GetMapping(value = "/{prefix}/{name}")
    public AjaxResult reverseIntGet(@PathVariable("prefix") String prefix, @PathVariable("name") String name, HttpServletRequest request){
        //get请求参数=url参数
        Map<String, Object> inputParamMap = ParamUtils.getRequestParamMap(request);
        return commonMethod(prefix, name, inputParamMap,request);
    }

    /**
     * 执行post请求
     * @return
     */
    @PostMapping(value = "/{prefix}/{name}")
    public AjaxResult reverseIntPost(@PathVariable("prefix") String prefix, @PathVariable("name") String name, @RequestBody Map<String,Object> inputParamMap, HttpServletRequest request){
        //post请求参数=url参数+requestBody参数
        inputParamMap.putAll(ParamUtils.getRequestParamMap(request));
        return commonMethod(prefix, name, inputParamMap,request);
    }

    /**
     * get与post请求公共处理方法
     * @return
     */
    public AjaxResult commonMethod( String prefix, String name, Map<String,Object> inputParamMap, HttpServletRequest request){

        //进行安全校验
        IntfaceReverseHistory intfaceReverseHistory = checkAuth(request);

        String[] nameSplitArr = name.split(",");

        if(nameSplitArr.length>10){
            throw new ServiceException("合并请求不能超过十个");
        }

        if(nameSplitArr.length==1){
            return AjaxResult.success(reverseIntService.executeMethod(prefix,name,inputParamMap,intfaceReverseHistory));
        }

        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        List<CompletableFuture<?>> futureAll = new ArrayList<>();

        for (int i = 0; i < nameSplitArr.length; i++) {
            int index = i;
            futureAll.add(CompletableFuture.runAsync(() -> {
                Map<String,Object>  resultMapTemp = (Map<String, Object>) reverseIntService.executeMethod(prefix, nameSplitArr[index], inputParamMap,intfaceReverseHistory);
                resultMap.put("result"+(index+1),resultMapTemp.get("result"));
            }));
        }

        //等待全部执行完成
        CompletableFuture.allOf(futureAll.toArray(new CompletableFuture[0])).join();

        return AjaxResult.success(resultMap);
    }

    public IntfaceReverseHistory checkAuth(HttpServletRequest request){
        IntfaceReverseHistory intfaceReverseHistory = new IntfaceReverseHistory();

        String authType = request.getHeader("authType");
        if(StrUtil.isBlank(authType)){
            authType = "authToken";
        }

        if(StrUtil.isNotBlank(request.getHeader("Authorization"))){
            authType = "authToken";
        }

        if("authToken".equals(authType)){
            String authToken = configService.selectConfigByKey("sys.check.authToken");
            if(StringUtils.isEmpty(authToken)||"on".equals(authToken)){
                LoginUser loginUser = SecurityUtils.getLoginUser();
                intfaceReverseHistory.setType(1L);
                intfaceReverseHistory.setReverseUserId(loginUser.getUserId());
                intfaceReverseHistory.setReverseUserName(loginUser.getUsername());
            }else{
                //如果不检查token的话,将其设置为开放调用
                intfaceReverseHistory.setType(4L);
            }
        }

        if("authCode".equals(authType)){
            String authCode = configService.selectConfigByKey("sys.check.authCode");
            if(StringUtils.isEmpty(authCode)||"off".equals(authCode)){
                throw new ServiceException("暂未开启授权校验");
            }

            String clientId = request.getHeader("clientId");
            String clientSecret = request.getHeader("clientSecret");

            if(StrUtil.isBlank(clientId)){
                throw new ServiceException("clientId不能为空");
            }

            if(StrUtil.isBlank(clientSecret)){
                throw new ServiceException("clientSecret不能为空");
            }

            IntfaceAppApplyFor intfaceAppApplyForTemp =  new IntfaceAppApplyFor();
            intfaceAppApplyForTemp.setClientId(clientId);
            intfaceAppApplyForTemp.setClientSecret(clientSecret);

            IntfaceAppApplyFor intfaceAppApplyFor = intfaceAppApplyForService.selectAppApplyBySecret(intfaceAppApplyForTemp);
            if(null==intfaceAppApplyFor){
                throw new ServiceException("密钥错误");
            }

            //计算两个日期时间差
            int different = DateUtils.differentDaysByMillisecond(intfaceAppApplyFor.getClientStartTime(), new Date());

            Integer expireDay = intfaceAppApplyFor.getClientExpireDay();

            if(different>expireDay){
                throw new ServiceException("密钥已失效");
            }
            intfaceReverseHistory.setType(2L);
            //当以密钥方式登录时,调用人=密钥应用创建人
            intfaceReverseHistory.setReverseUserId(intfaceAppApplyFor.getCreateId());
            intfaceReverseHistory.setReverseUserName(intfaceAppApplyFor.getCreateBy());
            intfaceReverseHistory.setReverseAppApplyForId(intfaceAppApplyFor.getId());
            intfaceReverseHistory.setReverseAppApplyForName(intfaceAppApplyFor.getName());


            //验证是否开启ip限制
            String openAccessIp = configService.selectConfigByKey("sys.authCode.open.accessIp");
            if(StringUtils.isEmpty(openAccessIp)||"on".equals(openAccessIp)){
                String clientIP = ServletUtil.getClientIP(request, null);
                if(StrUtil.isNotBlank(intfaceAppApplyFor.getAccessIp())){
                    String[] ipStr = intfaceAppApplyFor.getAccessIp().split(",");
                    if(false==Arrays.asList(ipStr).contains(clientIP)){
                        throw new ServiceException("当前ip禁止访问");
                    }
                }
            }
        }
        return intfaceReverseHistory;
    }

}
