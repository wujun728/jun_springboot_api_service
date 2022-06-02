package com.demo.dubbo.service;

import org.springframework.stereotype.Component;

import com.nh.micro.service.InjectGroovy;

@Component(value="processData")
@InjectGroovy(name="ProcessData")
public interface IProcessData {  
    public String deal(String data);  
} 