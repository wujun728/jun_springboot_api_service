/*
package com.example.demo.groovy.calculate.impl;

import com.example.demo.entity.request.CalculateRequest;
import com.example.demo.entity.response.CalculateResponse;
import com.example.demo.groovy.calculate.CalculateParser;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * 计算推广奖金
 *//*

public class RewardCalculateParser implements CalculateParser {

    @Override
    public CalculateResponse parse(CalculateRequest request) {

        Map<String, Object> extendInfo = request.getExtendInfo();

        String interfaceId = request.getInterfaceId();

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (StringUtils.isNotBlank((String) extendInfo.get("totalAmount"))) {
            totalAmount = new BigDecimal((String) extendInfo.get("totalAmount"));
        }

        int refererNumber = 0;
        if (StringUtils.isNotBlank((String) extendInfo.get("refererNumber"))) {
            refererNumber = Integer.parseInt((String) extendInfo.get("refererNumber"));
        }


        System.out.println("进入奖金计算逻辑,总金额为：" + totalAmount + "，邀请人数为：" + refererNumber);

        BigDecimal reward = totalAmount.multiply(new BigDecimal(String.valueOf(refererNumber)))
                .divide(new BigDecimal("100")).divide(new BigDecimal("365"),4, RoundingMode.HALF_DOWN);
        CalculateResponse response = new CalculateResponse();

        response.setInterfaceId(interfaceId);
        Map<String, Object> map = new HashMap<>();
        map.put("reward", reward);

        response.setExtendInfo(map);

        System.out.println("退出奖金计算逻辑,总奖金为：" + reward);
        return response;
    }
}
*/
