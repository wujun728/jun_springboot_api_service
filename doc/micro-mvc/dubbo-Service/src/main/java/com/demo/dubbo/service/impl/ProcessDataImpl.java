package com.demo.dubbo.service.impl;

import com.demo.dubbo.service.IProcessData;
import com.nh.micro.service.InjectGroovy;

@InjectGroovy(name="ProcessDataImpl")
public interface ProcessDataImpl extends IProcessData {  

    public String deal(String data);
}  