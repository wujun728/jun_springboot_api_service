package com.jun.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jun.plugin.system.service.ISysGeneratorService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestApplicationTests {
	
	@Resource
    private ISysGeneratorService sysGeneratorService;

    @Test
    public void contextLoads() throws IOException {
    	String tables = "biz_customer_test";
    	byte[] data = sysGeneratorService.generatorCode(tables.split(","));
    	FileUtils.writeByteArrayToFile(new File("D:/"+tables+".zip"), data);
    	log.info("代码生成成功！");
    }
    
    
    
    private void testCommonIO() throws IOException {
    	//读取文件
        String s = FileUtils.readFileToString(new File("output.txt") , "GBK");
        System.out.println(s);
        System.out.println("-----------------");
        byte[] datas = FileUtils.readFileToByteArray(new File("output.txt"));
        System.out.println(datas.length);
        System.out.println("-----------------");
        //逐行读取
        List<String> list = FileUtils.readLines(new File("output.txt"),"GBK");
        for(String temp:list) {
            System.out.println(temp);
        }
        System.out.println("-----------------");
        //写文件  追加
        FileUtils.write(new File("output.txt"), "锄禾日当午\r\n","GBK");
        FileUtils.write(new File("output.txt"), "汗滴禾下土","GBK",true);

	}

}
