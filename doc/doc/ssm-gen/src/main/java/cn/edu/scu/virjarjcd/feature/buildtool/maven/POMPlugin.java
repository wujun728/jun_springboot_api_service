package cn.edu.scu.virjarjcd.feature.buildtool.maven;

import cn.edu.scu.virjarjcd.mybatis.MergePluginAdpter;
import cn.edu.scu.virjarjcd.mybatis.formater.xml.VirjarXmlFormater;
import cn.edu.scu.virjarjcd.util.ConfigHolder;
import cn.edu.scu.virjarjcd.util.TextNodeBuilder;
import cn.edu.scu.virjarjcd.util.XmlElementChainBuider;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class POMPlugin extends MergePluginAdpter {

    private String projectRoot;
    private String projectName;
    private String basePakage;
    private XmlFormatter pomXmlFormatter;


    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {
        // TODO Auto-generated method stub
        if (ConfigHolder.instance.getProperty("project.moduleType", "single").equals("single")) {
            List<GeneratedXmlFile> answer = new ArrayList<>();
            Document document = new Document(null, null);
            XmlElement rootElement = new XmlElement("project");
            document.setRootElement(rootElement);
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, "pom.xml", "", projectRoot, true, pomXmlFormatter);
            answer.add(gxf);

            addSchema(rootElement);
            addDefine(rootElement);
//            addDefaultProperties(rootElement);
//            addDependency(rootElement);
            addDependencyWithManager(rootElement);
            addProfiles(rootElement);
            addPlugins(rootElement);
            //addRepositories(rootElement);
            //addPluginRepositories(rootElement);

            return answer;
        } else {
            return new MultiModulePomGenerator(pomXmlFormatter).genMuiltPoms();
        }


    }

    public static void addProfiles(XmlElement rootElement){
        new XmlElementChainBuider(rootElement)
                .addNode(
                        new XmlElementChainBuider("profiles")
                        .addNode(
                                new XmlElementChainBuider("profile")
                                .addNode(
                                        new XmlElementChainBuider("id")
                                                .addTextnode("local")
                                ).addNode(
                                        new XmlElementChainBuider("properties")
                                                .addNode(
                                                        new XmlElementChainBuider("deploy.type")
                                                                .addTextnode("local")
                                                )
                                ).addNode(
                                        new XmlElementChainBuider("activation")
                                        .addNode(
                                                new XmlElementChainBuider("activeByDefault")
                                                .addTextnode("true")
                                        )
                                )
                        ).addNode(
                                new XmlElementChainBuider("profile")
                                        .addNode(
                                                new XmlElementChainBuider("id")
                                                        .addTextnode("dev")
                                        ).addNode(
                                        new XmlElementChainBuider("properties")
                                                .addNode(
                                                        new XmlElementChainBuider("deploy.type")
                                                                .addTextnode("dev")
                                                )
                                )
                        ).addNode(
                                new XmlElementChainBuider("profile")
                                        .addNode(
                                                new XmlElementChainBuider("id")
                                                        .addTextnode("beta")
                                        ).addNode(
                                        new XmlElementChainBuider("properties")
                                                .addNode(
                                                        new XmlElementChainBuider("deploy.type")
                                                                .addTextnode("beta")
                                                )
                                )
                        ).addNode(
                                new XmlElementChainBuider("profile")
                                        .addNode(
                                                new XmlElementChainBuider("id")
                                                        .addTextnode("prod")
                                        ).addNode(
                                        new XmlElementChainBuider("properties")
                                                .addNode(
                                                        new XmlElementChainBuider("deploy.type")
                                                                .addTextnode("prod")
                                                )
                                )
                        )
                )
                .build();
    }

    private void addDependencyWithManager(XmlElement xmlElement){

        MavenDependencyManager mavenDependencyManager = MavenDependencyManager.buildDefaultDependencyManager();
        mavenDependencyManager.buildPropertiesNode(xmlElement, MavenDependencyManager.MuduleType.ALL);
        mavenDependencyManager.buildDependencyManagement(xmlElement, MavenDependencyManager.MuduleType.ALL);
        mavenDependencyManager.buildDependencies(xmlElement, MavenDependencyManager.MuduleType.ALL);
    }






    public static void addDefaultProperties(XmlElement rootElement){
        XmlElement propertiesElement = new XmlElement("properties");
        rootElement.addElement(propertiesElement);

        new TextNodeBuilder(propertiesElement)
                .addNode("java.source.version", "1.7")
                .addNode("java.target.version", "1.7")
                .addNode("org.springframework.version", "4.1.6.RELEASE")
                .addNode("fasterxml.jackson.version", "2.5.3")
                .addNode("mysql.driver.version", "5.1.29")
                .addNode("ch.qos.logback.verison", "1.1.3")
                .addNode("org.slf4j.version", "1.7.12")
                .addNode("com.alibaba.fastjson.verison", "1.1.31")
                .addNode("org.mybatis.mybatis.verison", "3.3.0")
                .addNode("org.mybatis.mybatis-spring.version", "1.2.2")
                .addNode("ma.glasnost.orika.version", "1.4.5")
                .addNode("servlet-api.version", "2.5")
                .addNode("org.apache.commons.version", "3.4")
                .addNode("commons-io.version", "2.4")
                .addNode("org.hibernate.hibernate-validator.version", "5.0.2.Final")
                .addNode("com.google.guava.version", "18.0")
                .build();

    }

    public static void addPluginRepositories(XmlElement rootElement){
    	XmlElement repositories = new XmlElement("pluginRepositories");
    	rootElement.addElement(repositories);

    	repositories.addElement(tomcatPluginRepository());
    }

    public static void addRepositories(XmlElement rootElement){
    	XmlElement repositories = new XmlElement("repositories");
    	rootElement.addElement(repositories);

    	repositories.addElement(tomcatRepository());

    }

    public static void addPlugins(XmlElement rootElement){
    	XmlElement build = new XmlElement("build");
    	rootElement.addElement(build);
    	XmlElement finalName = new XmlElement("finalName");
    	finalName.addElement(new TextElement(ConfigHolder.instance.getProperty("sys.projectName")));
    	build.addElement(finalName);

        new XmlElementChainBuider(build)
                .addNode(
                        new XmlElementChainBuider("resources")
                        .addNode(
                                new XmlElementChainBuider("resource")
                                .addNode(
                                        new XmlElementChainBuider("directory")
                                        .addTextnode("src/main/resources.${deploy.type}")
                                )
                        )
                        .addNode(
                                new XmlElementChainBuider("resource")
                                .addNode(
                                        new XmlElementChainBuider("directory")
                                                .addTextnode("src/main/resources")
                                )
                        )
                )
                .build();

    	XmlElement plugins = new XmlElement("plugins");
    	build.addElement(plugins);
    	plugins.addElement(mavenCompilePlugin());
    	//plugins.addElement(tomcatPlugin());
    }


    public static class Dependency {
        private String groupId;
        private String artifactId;
        private String version;
        private String scope;
        private String type;

        public Dependency(String groupId, String artifactId) {
            this.groupId = groupId;
            this.artifactId = artifactId;
        }

        public Dependency(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public Dependency setScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Dependency setType(String type) {
            this.type = type;
            return this;
        }

        public XmlElement buildXmlElement() {
            XmlElement xmlElement = new XmlElement("dependency");

            XmlElement groupElement = new XmlElement("groupId");
            groupElement.addElement(new TextElement(groupId));
            xmlElement.addElement(groupElement);

            XmlElement artifactElement = new XmlElement("artifactId");
            artifactElement.addElement(new TextElement(artifactId));
            xmlElement.addElement(artifactElement);

            if (!StringUtils.isEmpty(version)) {
                XmlElement versionElement = new XmlElement("version");
                versionElement.addElement(new TextElement(version));
                xmlElement.addElement(versionElement);
            }

            if (!StringUtils.isEmpty(scope)) {
                XmlElement scopeElement = new XmlElement("scope");
                scopeElement.addElement(new TextElement(scope));
                xmlElement.addElement(scopeElement);
            }

            if (!StringUtils.isEmpty(type)) {
                XmlElement typeElement = new XmlElement("type");
                typeElement.addElement(new TextElement(type));
                xmlElement.addElement(typeElement);
            }
            return xmlElement;
        }
    }

    private void addDependency(XmlElement xmlElement) {
        XmlElement dependecysElement = new XmlElement("dependencies");
        dependecysElement.addElement(new Dependency("mysql", "mysql-connector-java", "5.1.35").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.slf4j", "slf4j-api", "1.7.12").buildXmlElement());
        dependecysElement.addElement(new Dependency("ch.qos.logback", "logback-classic", "1.1.3").buildXmlElement());
        dependecysElement.addElement(new Dependency("ch.qos.logback", "logback-core", "1.1.3").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.slf4j", "jcl-over-slf4j", "1.7.5").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.slf4j", "log4j-over-slf4j", "1.7.5").buildXmlElement());

        dependecysElement.addElement(new Dependency("org.springframework", "spring-context", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework", "spring-web", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework", "spring-webmvc", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework", "spring-beans", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework", "spring-core", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-commons", "1.10.0.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-jpa", "1.8.0.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework.data", "spring-data-redis", "1.5.0.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.springframework", "spring-tx", "4.1.6.RELEASE").buildXmlElement());
        dependecysElement.addElement(new Dependency("com.alibaba", "fastjson", "1.1.31").setType("jar").setScope("compile").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.mybatis", "mybatis", "3.3.0").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.mybatis", "mybatis-spring", "1.2.2").buildXmlElement());
        dependecysElement.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-core", "2.5.3").buildXmlElement());
        dependecysElement.addElement(new Dependency("com.fasterxml.jackson.core", "jackson-databind", "2.5.3").buildXmlElement());
        dependecysElement.addElement(new Dependency("ma.glasnost.orika", "orika-core", "1.4.5").buildXmlElement());
        dependecysElement.addElement(new Dependency("javax.servlet", "servlet-api", "2.5").setScope("provided").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.apache.commons", "commons-lang3", "3.4").buildXmlElement());
        dependecysElement.addElement(new Dependency("commons-io", "commons-io", "2.4").buildXmlElement());
        dependecysElement.addElement(new Dependency("org.hibernate", "hibernate-validator", "5.0.2.Final").buildXmlElement());
        dependecysElement.addElement(new Dependency("com.google.guava", "guava", "18.0").buildXmlElement());

        if (ConfigHolder.isLombok()) {
            dependecysElement.addElement(new Dependency("org.projectlombok", "lombok", "1.16.6").buildXmlElement());
        }

        xmlElement.addElement(dependecysElement);
    }

    private void addDefine(XmlElement xmlElement) {
        new XmlElementChainBuider(xmlElement)

                .addNode(
                        new XmlElementChainBuider("groupId")
                                .addNode(
                                        new XmlElementChainBuider((String) null).addContent(basePakage)
                                )
                )
                .addNode(
                        new XmlElementChainBuider("artifactId")
                                .addNode(
                                        new XmlElementChainBuider((String) null).addContent(projectName)
                                )
                )
                .addNode(
                        new XmlElementChainBuider("packaging")
                                .addNode(
                                        new XmlElementChainBuider((String) null).addContent("war")
                                )
                )
                .addNode(
                        new XmlElementChainBuider("version")
                                .addNode(
                                        new XmlElementChainBuider((String) null).addContent("0.0.1-SNAPSHOT")
                                )
                )
                .build();
    }

    public static XmlElement mavenCompilePlugin(){
    	XmlElement answer = new XmlElement("plugin");
    	new XmlElementChainBuider(answer)
    	.addNode(
    			new XmlElementChainBuider("groupId").addTextnode("org.apache.maven.plugins")
    			)
    	.addNode(
    			new XmlElementChainBuider("artifactId").addTextnode("maven-compiler-plugin")
    			)
    	.addNode(
    			new XmlElementChainBuider("version").addTextnode("3.5.1")
    			)
    	.addNode(
    			new XmlElementChainBuider("configuration")
    			.addNode(
                        new XmlElementChainBuider("source").addTextnode("1.7")
                )
    			.addNode(
                        new XmlElementChainBuider("target").addTextnode("1.7")
                )
    			)
    	.build();
    	return answer;
    }

    public static XmlElement tomcatRepository(){
    	XmlElement answer = new XmlElement("repository");
    	new XmlElementChainBuider(answer)
    	.addNode(
    			new XmlElementChainBuider("id").addTextnode("people.apache.snapshots")
    			)
    	.addNode(
    			new XmlElementChainBuider("url").addTextnode("http://repository.apache.org/content/groups/snapshots-group/")
    			)
    	.addNode(
    			new XmlElementChainBuider("releases")
    			.addNode(
                        new XmlElementChainBuider("enabled").addTextnode("false")
                )
    			)
    	.addNode(
    			new XmlElementChainBuider("snapshots")
    			.addNode(
                        new XmlElementChainBuider("enabled").addTextnode("true")
                )
    			)
    	.build();
    	return answer;
    }

    public static XmlElement tomcatPluginRepository(){
    	XmlElement answer = new XmlElement("pluginRepository");
    	new XmlElementChainBuider(answer)
    	.addNode(
    			new XmlElementChainBuider("id").addTextnode("apache.snapshots")
    			)
    	.addNode(
    			new XmlElementChainBuider("name").addTextnode("Apache Snapshots")
    			)
    	.addNode(
    			new XmlElementChainBuider("url").addTextnode("http://repository.apache.org/content/groups/snapshots-group/")
    			)
    	.addNode(
    			new XmlElementChainBuider("releases")
    			.addNode(
                        new XmlElementChainBuider("enabled").addTextnode("false")
                )
    			)
    	.addNode(
    			new XmlElementChainBuider("snapshots")
    			.addNode(
                        new XmlElementChainBuider("enabled").addTextnode("true")
                )
    			)
    	.build();
    	return answer;
    }

    public static XmlElement tomcatPlugin(){
    	XmlElement answer = new XmlElement("plugin");
    	new XmlElementChainBuider(answer)
    	.addNode(
    			new XmlElementChainBuider("groupId").addTextnode("org.apache.tomcat.maven")
    			)
    	.addNode(
    			new XmlElementChainBuider("artifactId").addTextnode("tomcat7-maven-plugin")
    			)
    	.addNode(
    			new XmlElementChainBuider("version").addTextnode("2.2")
    			)
    	.addNode(
    			new XmlElementChainBuider("configuration")
    			.addNode(
                        new XmlElementChainBuider("url").addTextnode("http://localhost:8080/manager/html")
                )
    			.addNode(
                        new XmlElementChainBuider("server").addTextnode("tomcat7")
                )
    			.addNode(
                        new XmlElementChainBuider("port").addTextnode("8080")
                )
    			.addNode(
                        new XmlElementChainBuider("uriEncoding").addTextnode("UTF-8")
                )
    			.addNode(
                        new XmlElementChainBuider("username").addTextnode("admin")
                )
    			.addNode(
                        new XmlElementChainBuider("password").addTextnode("admin")
                )
    			)
    	.build();
    	return answer;
    }
    public static void addSchema(XmlElement xmlElement) {
        new XmlElementChainBuider(xmlElement)
                .addAttribute("xmlns", "http://maven.apache.org/POM/4.0.0")
                .addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
                .addAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd")
                .addNode(
                        new XmlElementChainBuider("modelVersion").addTextnode("4.0.0")
                ).addNode(
                new XmlElementChainBuider("url").addTextnode("http://maven.apache.org")
        ).build();
    }

    @Override
    public boolean calcEnv() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        projectRoot = ConfigHolder.instance.getProperty("sys.projectRootPath");
        projectName = ConfigHolder.instance.getProperty("sys.projectName");
        basePakage = ConfigHolder.instance.getProperty("sys.basePackage");

        if (ConfigHolder.instance.getProperty("formater.pom", "mybatis").trim().equalsIgnoreCase("jcg")) {
            pomXmlFormatter = new VirjarXmlFormater();
            pomXmlFormatter.setContext(getContext());
        } else {
            pomXmlFormatter = getContext().getXmlFormatter();
        }
        return true;
    }
}
