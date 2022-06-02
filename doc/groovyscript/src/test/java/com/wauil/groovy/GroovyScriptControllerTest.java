/*
 * @Copyright:  2019 dxxld@qq.com Personal.
 * All Rights Reserved.
 */

package com.wauil.groovy;

import com.wauil.groovy.config.GroovyScriptAutoConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description: groovy 脚本引擎测试
 * @Author: Bin.Zhou
 * @Email: dxxld@qq.com
 * @Date: 2019/02/14 16:16
 * @Version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = GroovyScriptAutoConfiguration.class)
public class GroovyScriptControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test() throws Exception {
        String groovyScript = "spring.getBean(\"scriptEngineManager\").toString()";
        MvcResult mvcResult = mockMvc.perform(post("/groovy").param("script", groovyScript))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
