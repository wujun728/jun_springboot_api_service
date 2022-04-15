package com.jun.plugin.system;

import java.io.File;
import java.io.IOException;
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
public class CodeGeneratorTest {
	
	@Resource
    private ISysGeneratorService sysGeneratorService;

    @Test
    public void contextLoads() throws IOException {
//    	String tables = "biz_test";
//    	String tables = "biz_common";
//    	String tables = "sys_post,biz_common,biz_mail,biz_test,hr_templet_assessment,hr_templet_assessment_userscore,hr_templet_quota_detail,hr_templet_quota_detail_userscore_detail,oa_law_info,oa_learn_info,oa_notes_info,oa_office_count,oa_poms_workmarks_outsite,oa_poms_workmarks_payroll,oa_poms_workmarks_times,oa_user_leave,oa_user_worktimes,pj_contract,pj_customer,pj_project,pj_project_appraise,pj_project_borrow,pj_project_daily,pj_project_draft,pj_project_invoice,pj_project_member,pj_project_plan,pj_project_prodess_risk,pj_project_recheck,pj_project_report,pj_project_reportnumber";
//    	String tables = "sys_post";
//    	String tables = "pj_customer";
//    	String tables = "pj_project";
//    	String tables = "pj_contract";
//    	String tables = "pj_project_plan";
//    	String tables = "pj_project_member";
//    	String tables = "pj_project_prodess_task";
//    	String tables = "pj_project_daily";
//    	String tables = "pj_project_draft";	
//    	String tables = "pj_project_report";
//    	String tables = "pj_project_reportnumber";
//    	String tables = "pj_project_appraise";
//    	String tables = "pj_project_borrow";
//    	String tables = "pj_project_invoice";
    	
//    	String tables = "pj_project_recheck";
    	
//    	String tables = "oa_office_count";
//    	String tables = "oa_poms_workmarks_leave,oa_poms_workmarks_outsite,oa_poms_workmarks_payroll,oa_poms_workmarks_worktimes";
    	
    	
//    	String tables = "hr_user_hire";
//    	String tables = "oa_office_count2";
    	String tables = "biz_test";
//    	String tables = "hr_assessment_template,hr_assessment_template_detail,hr_assessment_user_record_detail,hr_assessment_user_record";
    	
    	
    	
//    	String tables = "oa_poms_workmarks_claim_expense";
//    	String tables = "hr_user_become_member,hr_user_dimission,hr_user_entry_reported,hr_user_hire,hr_user_interview,hr_user_offer,hr_user_resume";
//    	String tables = "hr_templet_assessment,hr_templet_assessment_userscore,hr_templet_quota_detail,hr_templet_quota_detail_userscore_detail";
//    	
    	byte[] data = sysGeneratorService.generatorCode(tables.split(","));
    	FileUtils.writeByteArrayToFile(new File("D:/"+1111+"1202"+(new Random().nextInt(999))+".zip"), data);
    	log.info("代码生成成功！");
    }
    
    
    
    

}
