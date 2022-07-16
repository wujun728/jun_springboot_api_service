package com.jun.plugin.groovy;

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.jun.plugin.groovy.Param;
 
/**
 * groove class
 */
class TestGroovy {
 
    void print() {
        System.out.println("hello word!!!!");
    }
 
    List<String> printArgs(String str1, String str2, String str3) {
        String jsonString = "[\""+str1+"\",\""+str2+"\",\""+str3+"\"]";
        return JSON.parseObject(jsonString, new TypeReference<List<String>>() {});
    }
    void sayHello(Param p) {
		System.out.println("年龄是:" + p.getMsg());
	}
 
}