package cn.edu.scu.virjarjcd.feature.buildtool.maven;

import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import cn.edu.scu.virjarjcd.feature.buildtool.maven.POMPlugin.Dependency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virjar on 16/3/27.
 */
public class MultiModulePomGenerator {
    private XmlFormatter pomXmlFormatter;
    private String basePakage = ConfigHolder.instance.getProperty("sys.basePackage");
    private String projectName = ConfigHolder.instance.getProperty("sys.projectName");
    MavenDependencyManager mavenDependencyManager = MavenDependencyManager.buildDefaultDependencyManager();


    public MultiModulePomGenerator(XmlFormatter pomXmlFormatter) {
        this.pomXmlFormatter = pomXmlFormatter;

    }

    public List<GeneratedXmlFile> genMuiltPoms() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        Document rootPomDocument = genDocumentStructure();
        answer.add(genPomStructure(ConfigHolder.instance.getProperty("sys.projectRootPath"), rootPomDocument));
        genRootPom(rootPomDocument.getRootElement());

        //在root build完成之后再进行子模块相互依赖的构建，
        mavenDependencyManager.addDependency(MavenDependencyManager.MuduleType.SERVICE,
                new MavenDependency(basePakage,projectName + "-service-api","0.0.1-SNAPSHOT")
                        .setVersionKey("project")
                .forceVersion(true)
        );
        mavenDependencyManager.addDependency(MavenDependencyManager.MuduleType.WEBAPP,
                new MavenDependency(basePakage,projectName + "-service","0.0.1-SNAPSHOT")
                        .setVersionKey("project")
                .forceVersion(true)
        );
//        mavenDependencyManager.addDependency(MavenDependencyManager.MuduleType.ALL,
//                new MavenDependency(basePakage,projectName + "-webapp","0.0.1-SNAPSHOT")
//                        .setVersionKey("project")
//                .forceVersion(true)
//        );

        Document servicePomDocument = genDocumentStructure();
        answer.add(genPomStructure(ConfigHolder.instance.getProperty("sys.serviceRootPath"), servicePomDocument));
        genServicePom(servicePomDocument.getRootElement());

        Document serviceApiPomDocument = genDocumentStructure();
        answer.add(genPomStructure(ConfigHolder.instance.getProperty("sys.serviceApiRootPath"), serviceApiPomDocument));
        genServicrApiPom(serviceApiPomDocument.getRootElement());

        Document webappPomDocument = genDocumentStructure();
        answer.add(genPomStructure(ConfigHolder.instance.getProperty("sys.webappRootPath"), webappPomDocument));
        genWebAppPom(webappPomDocument.getRootElement());
        return answer;
    }

    private void genWebAppPom(XmlElement xmlElement) {
        addParentNode(xmlElement);

        new XmlElementChainBuider(xmlElement)
                .addNode(
                        new XmlElementChainBuider("artifactId").addTextnode(projectName + "-webapp")
                )
                .addNode(
                        new XmlElementChainBuider("packaging").addTextnode("war")
                )
                .build();
        mavenDependencyManager.buildDependencies(xmlElement, MavenDependencyManager.MuduleType.WEBAPP);
//        XmlElement dependecysElement = new XmlElement("dependencies");
//        dependecysElement.addElement(new Dependency(basePakage,projectName + "-service","").buildXmlElement());
//
//        dependecysElement.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-core").buildXmlElement());
//        xmlElement.addElement(dependecysElement);

        POMPlugin.addPlugins(xmlElement);
        // #1 tomcat其实不需要这个
        //POMPlugin.addRepositories(xmlElement);
        //POMPlugin.addPluginRepositories(xmlElement);

    }

    private void genServicePom(XmlElement xmlElement) {
        addParentNode(xmlElement);

        new XmlElementChainBuider(xmlElement)
                .addNode(
                        new XmlElementChainBuider("artifactId").addTextnode(projectName + "-service")
                )
                .addNode(
                        new XmlElementChainBuider("packaging").addTextnode("jar")
                )
                .build();
        mavenDependencyManager.buildDependencies(xmlElement, MavenDependencyManager.MuduleType.SERVICE);
//        XmlElement dependecysElement = new XmlElement("dependencies");
//        dependecysElement.addElement(new Dependency(basePakage,projectName + "-service-api","").buildXmlElement());
//        xmlElement.addElement(dependecysElement);
//
//        dependecysElement.addElement(new Dependency("mysql", "mysql-connector-java").setScope("runtime").buildXmlElement());
//        dependecysElement.addElement(new Dependency("com.alibaba", "fastjson").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.mybatis", "mybatis").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.mybatis", "mybatis-spring").buildXmlElement());
//
//
//        dependecysElement.addElement(new Dependency("commons-io", "commons-io").buildXmlElement());

    }

    private void genServicrApiPom(XmlElement xmlElement) {
        addParentNode(xmlElement);

        new XmlElementChainBuider(xmlElement)
                .addNode(
                        new XmlElementChainBuider("artifactId").addTextnode(projectName + "-service-api")
                )
                .addNode(
                        new XmlElementChainBuider("packaging").addTextnode("jar")
                )
                .build();
          mavenDependencyManager.buildDependencies(xmlElement, MavenDependencyManager.MuduleType.SERIVICEAPI);
//        XmlElement dependecysElement = new XmlElement("dependencies");
//
//
//        dependecysElement.addElement(new Dependency("org.slf4j", "slf4j-api").buildXmlElement());
//        dependecysElement.addElement(new Dependency("log4j", "log4j").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-context").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-web").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-webmvc").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-beans").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-core").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-commons").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-jpa").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-redis").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.springframework", "spring-tx").buildXmlElement());
//
//        dependecysElement.addElement(new Dependency("javax.servlet", "servlet-api").buildXmlElement());
//        dependecysElement.addElement(new Dependency("ma.glasnost.orika", "orika-core").buildXmlElement());
//        dependecysElement.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-databind").buildXmlElement());
//        dependecysElement.addElement(new Dependency("org.apache.commons", "commons-lang3").buildXmlElement());
//
//        if(ConfigHolder.isLombok()){
//            dependecysElement.addElement(new Dependency("org.projectlombok", "lombok").buildXmlElement());
//        }
//
//        xmlElement.addElement(dependecysElement);

    }

    private void addParentNode(XmlElement xmlElement) {
        new XmlElementChainBuider(xmlElement)
                .addNode(
                        new XmlElementChainBuider("parent")
                                .addNode(
                                        new XmlElementChainBuider("groupId").addTextnode(basePakage)
                                )
                                .addNode(
                                        new XmlElementChainBuider("artifactId").addTextnode(projectName)
                                )
                                .addNode(
                                        new XmlElementChainBuider("version").addTextnode("0.0.1-SNAPSHOT")
                                )
                )
                .build();
    }

    private Document genDocumentStructure() {
        Document document = new Document(null, null);
        XmlElement rootElement = new XmlElement("project");
        document.setRootElement(rootElement);
        POMPlugin.addSchema(rootElement);
        return document;
    }

    private GeneratedXmlFile genPomStructure(String filePath, Document document) {
        return new GeneratedXmlFile(document, "pom.xml", "", filePath, true, pomXmlFormatter);
    }

    private void genRootPom(XmlElement rootXmlElement) {
        new XmlElementChainBuider(rootXmlElement)
                .addNode(
                        new XmlElementChainBuider("groupId").addTextnode(basePakage)
                )
                .addNode(
                        new XmlElementChainBuider("artifactId").addTextnode(projectName)
                )
                .addNode(
                        new XmlElementChainBuider("packaging").addTextnode("pom")
                )
                .addNode(
                        new XmlElementChainBuider("version").addTextnode("0.0.1-SNAPSHOT")
                )
                .build();

        new XmlElementChainBuider(rootXmlElement)
                .addNode(
                        new XmlElementChainBuider("modules")
                                .addNode(
                                        new XmlElementChainBuider("module").addTextnode(projectName + "-service-api")
                                )
                                .addNode(
                                        new XmlElementChainBuider("module").addTextnode(projectName + "-service")
                                )
                                .addNode(
                                        new XmlElementChainBuider("module").addTextnode(projectName + "-webapp")
                                )
                )
                .build();

//        //properties
//        new XmlElementChainBuider(rootXmlElement)
//                .addNode(
//                        new XmlElementChainBuider("properties")
//                        .addNode(
//                                new XmlElementChainBuider("java_source_version").addTextnode("1.7")
//                        )
//                        .addNode(
//                                new XmlElementChainBuider("java_target_version").addTextnode("1.7")
//                        )
//                        .addNode(
//                                new XmlElementChainBuider("springframework_common").addTextnode("4.1.6.RELEASE")
//                        )
//                        .addNode(
//                                new XmlElementChainBuider("fasterxml_jackson").addTextnode("2.5.3")
//                        )
//
//                ).build();
//
//        XmlElement dependencies = new XmlElement("dependencies");
//        XmlElement dependencyManagement = new XmlElement("dependencyManagement");
//        dependencyManagement.addElement(dependencies);
//        rootXmlElement.addElement(dependencyManagement);
//
//        dependencies.addElement(new Dependency(basePakage,projectName + "-service-api","${project.version}").buildXmlElement());
//        dependencies.addElement(new Dependency(basePakage,projectName + "-service","${project.version}").buildXmlElement());
//        dependencies.addElement(new Dependency(basePakage,projectName + "-webapp","${project.version}").buildXmlElement());
//
//        dependencies.addElement(new Dependency("mysql", "mysql-connector-java", "5.1.35").buildXmlElement());
//        dependencies.addElement(new Dependency("org.slf4j", "slf4j-api", "1.7.12").buildXmlElement());
//        dependencies.addElement(new Dependency("log4j", "log4j", "1.2.17").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-context", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-web", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-webmvc", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-beans", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-core", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework.data", "spring-data-commons", "1.10.0.RELEASE").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework.data", "spring-data-jpa", "1.8.0.RELEASE").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework.data", "spring-data-redis", "1.5.0.RELEASE").buildXmlElement());
//        dependencies.addElement(new Dependency("org.springframework", "spring-tx", "${springframework_common}").buildXmlElement());
//        dependencies.addElement(new Dependency("com.alibaba", "fastjson", "1.1.31").setType("jar").setScope("compile").buildXmlElement());
//        dependencies.addElement(new Dependency("org.mybatis", "mybatis", "3.3.0").buildXmlElement());
//        dependencies.addElement(new Dependency("org.mybatis", "mybatis-spring", "1.2.2").buildXmlElement());
//        dependencies.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-core", "${fasterxml_jackson}").buildXmlElement());
//        dependencies.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-databind", "${fasterxml_jackson}").buildXmlElement());
//        dependencies.addElement(new Dependency("ma.glasnost.orika", "orika-core", "1.4.5").buildXmlElement());
//        dependencies.addElement(new Dependency("javax.servlet", "servlet-api", "2.5").setScope("provide").buildXmlElement());
//        dependencies.addElement(new Dependency("org.apache.commons", "commons-lang3", "3.4").buildXmlElement());
//        dependencies.addElement(new Dependency("commons-io", "commons-io", "2.4").buildXmlElement());
//
//        if(ConfigHolder.isLombok()){
//            dependencies.addElement(new Dependency("org.projectlombok", "lombok", "1.16.6").buildXmlElement());
//        }
//
//
        mavenDependencyManager.buildPropertiesNode(rootXmlElement, MavenDependencyManager.MuduleType.ALL);
        mavenDependencyManager.buildDependencyManagement(rootXmlElement, MavenDependencyManager.MuduleType.ALL);

        POMPlugin.addProfiles(rootXmlElement);
    }


}
