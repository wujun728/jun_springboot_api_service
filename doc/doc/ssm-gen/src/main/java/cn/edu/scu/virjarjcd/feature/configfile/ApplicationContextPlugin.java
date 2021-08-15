package cn.edu.scu.virjarjcd.feature.configfile;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class ApplicationContextPlugin extends MergePluginAdpter {

    private String basePackae;
    private String resourcePath;
    private XmlFormatter xmlFormatter;
    private String beanmappperPackage;


    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        // TODO Auto-generated method stub
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        Document document = new Document(null, null);
        XmlElement rootElement = new XmlElement("beans");
        GeneratedXmlFile gxf = new GeneratedXmlFile(document, "applicationContext.xml", "", resourcePath, true, xmlFormatter);
        answer.add(gxf);
        document.setRootElement(rootElement);
        addBeansSchema(rootElement);
        addContext(rootElement);
        addBean(rootElement);
        return answer;
    }

    private void addSpringDataSource(XmlElement rootElement) {
        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("bean")
                                .addAttribute("id", "dataSource")
                                .addAttribute("class", "org.springframework.jdbc.datasource.DriverManagerDataSource")
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "username")
                                                .addAttribute("value", "${resource.rdbms.mysql.username}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "password")
                                                .addAttribute("value", "${resource.rdbms.mysql.password}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "driverClassName")
                                                .addAttribute("value", "${resource.rdbms.mysql.driver}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "url")
                                                .addAttribute("value", "${resource.rdbms.mysql.url}")
                                )
                ).build();
    }

    private void addDruidDataSource(XmlElement rootElement) {
        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("bean")
                                .addAttribute("id", "dataSource")
                                .addAttribute("class", "com.alibaba.druid.pool.DruidDataSource")
                                .addAttribute("init-method", "init")
                                .addAttribute("destroy-method", "close")
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "url")
                                                .addAttribute("value", "${resource.rdbms.mysql.url}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "username")
                                                .addAttribute("value", "${resource.rdbms.mysql.username}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "password")
                                                .addAttribute("value", "${resource.rdbms.mysql.password}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "driverClassName")
                                                .addAttribute("value", "${resource.rdbms.mysql.driver}")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "maxActive")
                                                .addAttribute("value", "20")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "initialSize")
                                                .addAttribute("value", "1")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "maxWait")
                                                .addAttribute("value", "60000")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "minIdle")
                                                .addAttribute("value", "1")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "timeBetweenEvictionRunsMillis")
                                                .addAttribute("value", "3000")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "minEvictableIdleTimeMillis")
                                                .addAttribute("value", "300000")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "validationQuery")
                                                .addAttribute("value", "select count(1)")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "testWhileIdle")
                                                .addAttribute("value", "true")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "testOnBorrow")
                                                .addAttribute("value", "false")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "testOnReturn")
                                                .addAttribute("value", "false")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "filters")
                                                .addAttribute("value", "stat")
                                )
                )
                .build();
    }

    private void addBean(XmlElement rootElement) {

        if ("spring".equalsIgnoreCase(ConfigHolder.instance.getProperty("project.datasource"))) {
            addSpringDataSource(rootElement);
        } else {
            addDruidDataSource(rootElement);
        }

        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("bean")
                                .addAttribute("class", "org.mybatis.spring.mapper.MapperScannerConfigurer")
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "basePackage")
                                                .addAttribute("value", basePackae + ".**.repository")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "sqlSessionFactoryBeanName")
                                                .addAttribute("value", "myBatisSqlSessionFactory")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "annotationClass")
                                                .addAttribute("value", "org.springframework.stereotype.Repository")
                                )
                ).build();

        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("bean")
                                .addAttribute("id", "myBatisSqlSessionFactory")
                                .addAttribute("class", "org.mybatis.spring.SqlSessionFactoryBean")
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "dataSource")
                                                .addAttribute("ref", "dataSource")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "mapperLocations")
                                                .addAttribute("value", "classpath*:mapper/*Mapper.xml")
                                )
                                .addNode(
                                        new XmlElementChainBuider("property")
                                                .addAttribute("name", "configLocation")
                                                .addAttribute("value", "classpath:mybatis-config.xml")
                                )
                )
                .build();
        if (!ConfigHolder.isSingleLayer()) {
            new XmlElementChainBuider(rootElement)
                    .addNode(
                            new XmlElementChainBuider("bean")
                                    .addAttribute("id", "beanMapper")
                                    .addAttribute("class", beanmappperPackage + ".OrikaBeanMapper")
                                    .addNode(
                                            new XmlElementChainBuider("property")
                                                    .addAttribute("name", "basePackage")
                                                    .addAttribute("value", basePackae + ".model.**")
                                    )
                    )
                    .build();
        }
    }

    private void addContext(XmlElement rootElement) {
        rootElement.addElement(new XmlElementChainBuider("context:component-scan").addAttribute("base-package", basePackae + ".**.repository").build());
        rootElement.addElement(new XmlElementChainBuider("context:component-scan").addAttribute("base-package", basePackae + ".**.service").build());
        rootElement.addElement(new XmlElement("context:annotation-config"));
        rootElement.addElement(new XmlElementChainBuider("context:property-placeholder").addAttribute("location", "classpath:*.properties").build());
    }


    private void addBeansSchema(XmlElement rootElement) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://www.springframework.org/schema/beans ");
        sb.append("http://www.springframework.org/schema/beans/spring-beans-4.0.xsd ");
        sb.append("http://www.springframework.org/schema/context ");
        sb.append("http://www.springframework.org/schema/context/spring-context-4.0.xsd ");
        sb.append("http://www.springframework.org/schema/jdbc ");
        sb.append("http://www.springframework.org/schema/jdbc/spring-jdbc-4.0.xsd ");
        sb.append("http://www.springframework.org/schema/jee ");
        sb.append("http://www.springframework.org/schema/jee/spring-jee-4.0.xsd ");
        sb.append("http://www.springframework.org/schema/tx ");
        sb.append("http://www.springframework.org/schema/tx/spring-tx-4.0.xsd ");
        sb.append("http://www.springframework.org/schema/data/jpa ");
        sb.append("http://www.springframework.org/schema/data/jpa/spring-jpa-1.6.xsd ");

        new XmlElementChainBuider(rootElement)
                .addAttribute("xmlns", "http://www.springframework.org/schema/beans")
                .addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
                .addAttribute("xmlns:context", "http://www.springframework.org/schema/context")
                .addAttribute("xmlns:jdbc", "http://www.springframework.org/schema/jdbc")
                .addAttribute("xmlns:jee", "http://www.springframework.org/schema/jee")
                .addAttribute("xmlns:tx", "http://www.springframework.org/schema/tx")
                .addAttribute("xmlns:jpa", "http://www.springframework.org/schema/data/jpa")
                .addAttribute("default-lazy-init", "true")
                .addAttribute("xsi:schemaLocation", sb.toString())
                .build();
    }

    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        resourcePath = ConfigHolder.instance.getProperty("sys.webapp.resoucepath");
        basePackae = ConfigHolder.instance.getProperty("sys.basePackage", "com.virjar");
        beanmappperPackage = ConfigHolder.instance.getProperty("beanmappper.package");
        if (ConfigHolder.instance.getProperty("formater.spring", "mybatis").trim().equalsIgnoreCase("jcg")) {
            xmlFormatter = new VirjarXmlFormater();
            xmlFormatter.setContext(getContext());
        } else {
            xmlFormatter = getContext().getXmlFormatter();
        }
        return true;
    }
}
