package com.demo.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboProviderMain {  
 
    public static void main(String[] args) throws Exception {  
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[]{"applicationProvider.xml"});  
        context.start();
  
        System.out.println("Press any key to exit.");  
        System.in.read();  
    }  
}  