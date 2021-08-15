package cn.edu.scu.virjarjcd.feature.buildtool.maven;

import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.TextNodeBuilder;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by virjar on 16/3/31.
 */
public class MavenDependencyManager {

    private TreeSet<MavenDependency> dependencis = new TreeSet<>();
    private TreeSet<MavenDependency> rootDependencis = new TreeSet<>();
    private TreeSet<MavenDependency> webappDependencis = new TreeSet<>();
    private TreeSet<MavenDependency> serviceDependencis = new TreeSet<>();
    private TreeSet<MavenDependency> serviceapiDenpendencis = new TreeSet<>();

    public enum MuduleType {
        ROOT, WEBAPP, SERVICE, SERIVICEAPI, ALL
    }

    public void addDependency(MuduleType moduleType, MavenDependency dependency) {
        switch (moduleType) {
            case ROOT:
                rootDependencis.add(dependency);
                break;
            case WEBAPP:
                webappDependencis.add(dependency);
                break;
            case SERIVICEAPI:
                serviceapiDenpendencis.add(dependency);
                break;
            case SERVICE:
                serviceDependencis.add(dependency);
                break;
        }
        dependencis.add(dependency);
    }

    public void addDependency(MavenDependency dependency) {
        addDependency(MuduleType.ROOT, dependency);
    }

    private TreeSet<MavenDependency> getCollectionByType(MuduleType muduleType) {
        switch (muduleType) {
            case ALL:
                return dependencis;
            case ROOT:
                return rootDependencis;
            case WEBAPP:
                return webappDependencis;
            case SERVICE:
                return serviceDependencis;
            case SERIVICEAPI:
                return serviceapiDenpendencis;
        }
        return null;//not happened now
    }

    public void buildPropertiesNode(XmlElement rootElement, MuduleType muduleType) {
        TreeSet<MavenDependency> tempdependencis = getCollectionByType(muduleType);
        XmlElement properties = new XmlElement("properties");
        rootElement.addElement(properties);

        TextNodeBuilder textNodeBuilder = new TextNodeBuilder(properties);

        //对与相同版本key申明的依赖，做消重处理，以第一个出现的依赖为主
        Map<String, String> draftproperties = new HashMap<>();
        for (MavenDependency dependency : tempdependencis) {
            String versionKey = dependency.getVersionKey();
            if (!draftproperties.containsKey(versionKey)) {
                draftproperties.put(versionKey, dependency.getVersion());
                textNodeBuilder.addNode(versionKey, dependency.getVersion());
            }
        }
        textNodeBuilder.build();
    }

    public void buildDependencyManagement(XmlElement rootElement, MuduleType muduleType) {

        TreeSet<MavenDependency> tempdependencis = getCollectionByType(muduleType);
        XmlElement dependencyManagement = new XmlElement("dependencyManagement");
        rootElement.addElement(dependencyManagement);

        XmlElement dependencies = new XmlElement("dependencies");
        dependencyManagement.addElement(dependencies);

        for (MavenDependency dependency : tempdependencis) {
            XmlElement dependencyElement = new XmlElement("dependency");
            dependencies.addElement(dependencyElement);
            TextNodeBuilder textNodeBuilder = new TextNodeBuilder(dependencyElement)
                    .addNode("groupId", dependency.getGroupid())
                    .addNode("artifactId", dependency.getArctfictid())
                    .addNode("version", "${" + dependency.getVersionKey() + "}");

            if (dependency.getScope() != null) {
                textNodeBuilder.addNode("scope", dependency.getScope());
            }
            textNodeBuilder.build();

            if (dependency.getExclusions().size() > 0) {
                XmlElement exclusionsElement = new XmlElement("exclusions");
                dependencyElement.addElement(exclusionsElement);

                for (MavenDependency exclusion : dependency.getExclusions()) {
                    XmlElement exclusionElement = new XmlElement("exclusion");
                    exclusionsElement.addElement(exclusionElement);
                    new TextNodeBuilder(exclusionElement)
                            .addNode("groupId", exclusion.getGroupid())
                            .addNode("artifactId", exclusion.getArctfictid())
                            .build();
                }
            }
        }
    }

    public void buildDependencies(XmlElement rootElement, MuduleType muduleType) {
        TreeSet<MavenDependency> tempdependencis = getCollectionByType(muduleType);
        XmlElement dependencies = new XmlElement("dependencies");
        rootElement.addElement(dependencies);
        for (MavenDependency dependency : tempdependencis) {
            if(dependency.isOnlySetVersion()){
                continue;
            }
            XmlElement dependencyElement = new XmlElement("dependency");
            dependencies.addElement(dependencyElement);
            TextNodeBuilder textNodeBuilder = new TextNodeBuilder(dependencyElement)
                    .addNode("groupId", dependency.getGroupid())
                    .addNode("artifactId", dependency.getArctfictid());
            if (dependency.isForceSetVersion()) {
                textNodeBuilder.addNode("version", dependency.getVersion());
            }
            textNodeBuilder.build();
        }
    }

    public static MavenDependencyManager buildDefaultDependencyManager() {

        String springframework = "springframework";
        String fasterxml_jackson = "fasterxml.jackson";
        String qos_logback = "qos.logback";
        String slf4j = "slf4j";
        String httpcomponent = "httpcomponent";

        MavenDependencyManager mavenDependencyManager = new MavenDependencyManager();
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("mysql", "mysql-connector-java", "5.1.35").setScope("runtime"));

        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.slf4j", "slf4j-api", "1.7.12").setVersionKey(slf4j));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.slf4j", "jcl-over-slf4j", "1.7.12").setVersionKey(slf4j));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.slf4j", "log4j-over-slf4j", "1.7.12").setVersionKey(slf4j));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.slf4j", "jul-to-slf4j", "1.7.12").setVersionKey(slf4j));

        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("ch.qos.logback", "logback-classic", "1.1.3").setVersionKey(qos_logback));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("ch.qos.logback", "logback-core", "1.1.3").setVersionKey(qos_logback));

        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.springframework", "spring-core", "4.2.6.RELEASE")
                        .setVersionKey(springframework)
                        .addEceculison("commons-logging", "commons-logging")
        );
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.springframework", "spring-context", "4.2.6.RELEASE").setVersionKey(springframework));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.springframework", "spring-web", "4.2.6.RELEASE").setVersionKey(springframework));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.springframework", "spring-webmvc", "4.2.6.RELEASE").setVersionKey(springframework));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.springframework", "spring-beans", "4.2.6.RELEASE").setVersionKey(springframework));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.springframework", "spring-tx", "4.2.6.RELEASE").setVersionKey(springframework));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-aop", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        //mavenDependencyManager.addDependency(MuduleType.NONE, new MavenDependency("org.springframework", "spring-asm", "4.2.6.RELEASE").setVersionKey(springframework));
        //高版本本组件不存在
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-aspects", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-context-support", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-expression", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-instrument", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-jdbc", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-jms", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-oxm", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-orm", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.springframework", "spring-test", "4.2.6.RELEASE").setVersionKey(springframework).setOnlySetVersion(true));


        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.springframework.data", "spring-data-commons", "1.10.0.RELEASE"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.springframework.data", "spring-data-jpa", "1.8.0.RELEASE"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.springframework.data", "spring-data-redis", "1.5.0.RELEASE"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.mybatis", "mybatis", "3.3.0"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("org.mybatis", "mybatis-spring", "1.2.2"));

        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("com.fasterxml.jackson.core", "jackson-core", "2.5.3").setVersionKey(fasterxml_jackson));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("com.fasterxml.jackson.core", "jackson-databind", "2.5.3").setVersionKey(fasterxml_jackson));

        if(!ConfigHolder.isSingleLayer()) {
            mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("ma.glasnost.orika", "orika-core", "1.4.5"));
        }
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.apache.commons", "commons-lang3", "3.4"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("commons-io", "commons-io", "2.4"));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.hibernate", "hibernate-validator", "5.0.2.Final"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("com.google.guava", "guava", "18.0"));
        mavenDependencyManager.addDependency(MuduleType.SERVICE, new MavenDependency("com.alibaba", "fastjson", "1.1.31"));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("javax.servlet", "servlet-api", "2.5").setScope("provided"));

        if (ConfigHolder.isLombok()) {
            mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.projectlombok", "lombok", "1.16.6"));
        }
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI,new MavenDependency("com.alibaba","druid","1.0.18"));

        //httpclient 组件统一
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.apache.httpcomponents", "httpclient", "4.3")
                .setVersionKey(httpcomponent)
                .addEceculison("commons-logging", "commons-logging")
                .setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.apache.httpcomponents", "httpcore", "4.3").setVersionKey(httpcomponent).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.apache.httpcomponents", "httpmime", "4.3")
                .setVersionKey(httpcomponent)
                .addEceculison("commons-logging", "commons-logging") );
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.apache.httpcomponents", "httpcore-nio", "4.3").setVersionKey(httpcomponent).setOnlySetVersion(true));
        mavenDependencyManager.addDependency(MuduleType.ALL, new MavenDependency("org.apache.httpcomponents", "httpasyncclient", "4.1.1").setOnlySetVersion(true));

        //测试相关依赖
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("junit", "junit-dep", "4.10").setScope("test"));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("org.easymock", "easymock", "2.2").setScope("test"));
        mavenDependencyManager.addDependency(MuduleType.SERIVICEAPI, new MavenDependency("dbunit", "dbunit", "2.2").setScope("test")
        .addEceculison("commons-logging","commons-logging")
        .addEceculison("junit","junit"));

        return mavenDependencyManager;
    }
}
