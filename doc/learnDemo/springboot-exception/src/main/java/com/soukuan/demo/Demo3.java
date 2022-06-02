package com.soukuan.demo;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiebiao
 * @date 2021/9/18 18:38
 * @description
 */
@Component
public class Demo3 {

    @Resource
    private Demo2 demo2;
    @Resource
    private Demo1 demo1;

}
