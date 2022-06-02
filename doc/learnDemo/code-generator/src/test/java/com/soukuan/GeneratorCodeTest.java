package com.soukuan;

import com.soukuan.handler.CodeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringCodeGeneratorApplication.class})
public class GeneratorCodeTest {

    @Resource
    private CodeHandler codeHandler;

    @Test
    public void generatorCode() throws Exception {

        /**
         * 设置需要生成代码的表名
         * 1.项目里添加表格 如：tableNames = "bill_list,bill_order_rela";
         * 2.生成新项目 如：String tableNames = "*";
         */
        String tableNames = "t_user_info,sys_user,sys_role,sys_permission,sys_role_user,sys_permission_role";
        codeHandler.codeGeneratorHandler(tableNames);
    }

}
