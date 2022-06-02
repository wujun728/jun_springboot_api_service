package com.demo.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.dubbo.service.IProcessData;

public class ConsumerThd implements Runnable {  
  
 
    public void run() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[]{"applicationConsumer.xml"});  
        context.start();  
  
        IProcessData demoService = (IProcessData) context.getBean("demoService"); // get  
                                                                                // service  
                                                                                // invocation  
        // proxy  
        String hello = demoService.deal("nihao"); // do invoke!  
  
        System.out.println(Thread.currentThread().getName() + " "+hello);  
    }  
    
	public static void main(String[] args) {
		new Thread(new ConsumerThd()).start();
	}
}  