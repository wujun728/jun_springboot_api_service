micro-mvc框架，可以与springmvc\springcloud\dubbo或其他基于spring的mvc框架整合,
使所有的controller、servicebean、dao和sql业务逻辑代码都支持热部署方便开发人员调式和生产部署。


**与springmvc整合过程**

**编写Controller接口**

整合后Springmvc的controller只编写接口，参数名称必须用RequestParam注解。
使用InjectGroovy注解在接口中声明对应的groovy实现名称。
其他与传统springmvc的controller无异。


```
package foo.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nh.micro.service.InjectGroovy;

@Controller
@RequestMapping("test")
@InjectGroovy(name="TestController")
public interface TestController {
@RequestMapping("echo")
@ResponseBody
public  Map echo(@RequestParam(value="str") String str,HttpServletRequest httpRequest);

}
```


**编写Controller层的实现groovy**


```
package groovy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.service.InjectGroovy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;

import foo.service.TestService;


class TestController  {  
	@Resource
	public TestService testService;
	

	public Map echo(String str,HttpServletRequest httpRequest) {
		System.out.println("this is controller proxy");
		testService.test("111");
		Map retMap=new HashMap();
		retMap.put("status", "0");
		return retMap;
	}

}
```


**配置controller层包扫描**

使用GroovyBeanScannerConfigurer对controller的接口进行扫描（与component-scan不冲突可以重复配置，component-scan不会扫描接口）


```
	<bean class="com.nh.micro.service.GroovyBeanScannerConfigurer">
		<property name="scanPath" value="foo.web"></property>
	</bean>
<!-- <context:component-scan base-package="foo.web" /> -->
```

**Service层**

**编写ServiceBean接口**

并进行包扫描，使controller层能够是resource加载到ServiceBean代理对象。注意在接口中使用@InjectGroovy注解声明对应的实现业务逻辑的groovy文件名字
编写ServiceBean接口

```
package foo.service;
import com.nh.micro.service.InjectGroovy;
@InjectGroovy(name="TestService")
public interface TestService {
	public void test(String id);
}
```

**Service层groovy实现**

```
package groovy.service;
import javax.annotation.Resource;
import foo.dao.TestDao;
import foo.dto.MicroTestDto;
import foo.repository.*;
import groovy.json.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import com.nh.micro.dao.mapper.DefaultPageInfo;
import com.nh.micro.dao.mapper.InjectDao;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroServiceTemplateSupport;
import com.nh.micro.template.MicroTMProxy;


class TestService  {  
	
	@Resource
	public TestDao testDao;

	public void test(String id){
		
		Map paramMap=new HashMap();
		paramMap.put("id", id);
		MicroTestDto microTestDto=testDao.queryInfoById(paramMap);

		List<MicroTestDto> list=testDao.getInfoListAllMapper(microTestDto, ""); 
		
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		pageInfo.setPageNo(1);
		List<MicroTestDto> retList=testDao.queryInfosByPage(paramMap, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println("total="+total);

	}

}
```

**Service层包扫描配置**

```
	<bean class="com.nh.micro.service.GroovyBeanScannerConfigurer">
		<property name="scanPath" value="foo.service"></property>
	</bean>
```

**Dao层**

使用micro-dao模块，需要编写dto类，dao接口和sql文件。通过扫描sql文件和dao接口，使service层能够使用Resource注解加载dao代理对象。

**编写dto对象类**

使用@MicroTableName映射数据中表名，使用@MicroMappingAnno映射表中列名。

```
package foo.dto;

import java.util.Date;
import com.nh.micro.orm.MicroMappingAnno;
import com.nh.micro.orm.MicroTableName;

@MicroTableName(name="micro_test")
public class MicroTestDto {
	@MicroMappingAnno(name="id")
	private String id;
	@MicroMappingAnno(name="meta_key")
	private String metaKey;
	@MicroMappingAnno(name="meta_name")
	private String metaName;
	@MicroMappingAnno(name="meta_type")
	private String metaType;
	@MicroMappingAnno(name="create_time")
	private Date createTime;
	
	@MicroMappingAnno(name="meta_num")
	private Integer metaNum;

	public Integer getMetaNum() {
		return metaNum;
	}

	public void setMetaNum(Integer metaNum) {
		this.metaNum = metaNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
}
```

**编写Dao接口**
被扫描的dao接口需要使用@InjectDao注解

```
package foo.dao;
import java.util.List;
import java.util.Map;
import com.nh.micro.dao.mapper.ListInnerClass;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroPageInfo;
import com.nh.micro.orm.MicroDbName;
import foo.dto.MicroTestDto;

@InjectDao
@MicroDbName
public interface TestDao extends MicroCommonMapper<MicroTestDto> {
		public int updateInfo(Map paramMap);
		public int insertInfo(Map paramMap);
		@ListInnerClass(name=MicroTestDto.class)
	public List<MicroTestDto> queryInfosByPage(Map paramMap,MicroPageInfo pageInfo);	
	public MicroTestDto queryInfoById(Map paramMap);	
}
```

**Dao层包扫描配置**

```
	<bean id="daoScan" class="com.nh.micro.dao.mapper.scan.BeanScannerConfigurer">
		<property name="scanPath" value="foo.dao"></property>
	</bean>
```

**编写sql模板**

类似jsp的语法编写sql，支持#和$两种替换符。不必区分select还是update，统一用<sql id=”xxx”>标签，id与dao接口方法名一致，sql文件名与dao接口名一致。
```
<%! <sql id="queryInfoById"> %>
select * from micro_test where 1=1 
<% if(paramArray[0].get("id")!=null){ %>		
	and id = #{paramArray[0].id} 			
<% 	} %>
<%! </sql> %>

<%! <sql id="queryInfosByPage"> %>
select * from micro_test 
<%! </sql> %>

<%! <sql id="insertInfo"> %>
insert into micro_test(id,meta_key) values( 
?			
<% repList.add(paramArray[0].get("id"));%>
,?		
<% repList.add(paramArray[0].get("meta_key"));%>	
	)
<%! </sql> %>
 
<%! <sql id="updateInfo"> %>
update micro_test set  
			
<% if(paramArray[0].get("meta_key")!=null){%>
,meta_key=#{paramArray[0].get("meta_key")}	
<% } %>	
<% if(paramArray[0].get("meta_name")!=null){%>
,meta_name=#{paramArray[0].get("meta_name")}	
<% } %>	

where id=#{paramArray[0].get("id")}
<%! </sql> %>
```

**Sql文件扫描配置**

```
	<!-- micro-dao sql文件加载 -->
	<bean class="com.nh.micro.nhs.NhsInitUtil"
		init-method="initGroovyAndThread" lazy-init="false">
		<property name="fileList">
			<list>
				<bean class="com.nh.micro.rule.engine.core.GFileBean">
					<property name="ruleStamp" value="true"></property>
					<property name="jarFileFlag" value="true"></property>
					<property name="dirFlag" value="true"></property>
					<property name="rulePath" value="/groovy/"></property>
				</bean>
			</list>
		</property>
	</bean>
```

**MicroDao说明**

同时支持mysql和oracle
MicroDao相对mybatis的优点：
1，sql脚本支持修改后热部署实时生效。
2，bean与数据库字段映射关系，通过注解设置到bean中，不必在sql脚本中体现。
3，sql脚本支持类似jsp的写法，且不必区分select、update使用不同标签，更加灵活。
4，不需要使用插件，内置支持物理分页。
5，不需要使用插件，内置支持针对bean的标准增删改查功能。
6，不需要使用插件，内置支持读写分离，分库分表。
7，针对mysql5.7支持动态字段。
支持mapper、template、非orm三种模式支撑业务系统
1，mapper指，通过扫描接口，运行时自动生成dao实例；
2，template指，通过继承template标准父类，生成dao子类；
3，非orm指，直接使用microDao实例，可以执行非orm更灵活的数据库操作。

**关于事务**

由于serviceBean接口对象通过包扫描发布为springbean，所以仍可以使用原有的spring事务机制。

```
    <!-- 配置platform transaction manager-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean> 

    <!-- 声明式事物管理，配置事物管理advice-->
     <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>

            <tx:method name="get*" read-only="true"/>
<tx:method name="test*" propagation="REQUIRED"/>
            <tx:method name="add*" propagation="REQUIRED"/>
            <tx:method name="insert*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice> 

    <!-- 配置事物管理advice作用范围与作用条件-->
    <aop:config>
        <aop:pointcut id="serviceLayerTransaction" expression="execution( * foo.service.*..*(..))"/>
        <aop:advisor pointcut-ref="serviceLayerTransaction" advice-ref="txAdvice"/>
    </aop:config>
```

**SpringCloud整合**

SpringCloud整合controller，service、dao层与springmvc整合均一致。
具体查看demo项目
Micro-springcloud-mvc


