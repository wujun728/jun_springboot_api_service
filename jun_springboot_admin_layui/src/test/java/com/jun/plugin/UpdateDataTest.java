package com.jun.plugin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UpdateDataTest {
	
	@Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void contextLoads() throws IOException {
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(" select * from sys_user ");
    	list.forEach(item->{
    		log.error("item="+JSON.toJSONString(item));
    		item.forEach((key,value)->{
    			log.info("key="+key,"value="+value);
    			if(key.equalsIgnoreCase("full_name")) {
    				String id = String.valueOf(item.get("id"));
    				String username = PinYinUtil.getPinyin(String.valueOf(item.get("real_name")));
    				this.jdbcTemplate.update("update sys_user set username = ? where id = ? and username !='admin' ", new Object[] {username,id});
    			}
    		});
    	});
    }
    
    
    
    

}
