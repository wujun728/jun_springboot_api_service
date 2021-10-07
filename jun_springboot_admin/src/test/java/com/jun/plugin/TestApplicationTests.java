package com.jun.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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

//    @Test
    public void contextLoads() throws IOException {
    	String tables = "biz_customer_test";
//    	String tables = "oa_law_info,oa_learn_info,oa_notes_info";
//    	String tables = "pj_customer,pj_contract,pj_project";
//    	String tables = "hr_templet_assessment,hr_templet_assessment_userscore,hr_templet_quota_detail,hr_templet_quota_detail_userscore_detail";
//    	String tables = "oa_poms_workmarks,oa_poms_workmarks_payroll,oa_poms_workmarks_times";
//    	
//    	String tables = "pj_project_invoice,pj_project_member,pj_project_plan,pj_project_recheck,pj_project_report,pj_project_reportnumber";
    	byte[] data = sysGeneratorService.generatorCode(tables.split(","));
    	FileUtils.writeByteArrayToFile(new File("D:/"+"542543"+(new Random().nextInt(999))+".zip"), data);
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
