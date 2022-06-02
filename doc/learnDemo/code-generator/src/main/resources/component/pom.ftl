<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${basePackage}</groupId>
    <artifactId>${projectName}</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <modules>
        <module>${projectName}-api</module>
        <module>${projectName}-web</module>
    </modules>
    <packaging>pom</packaging>

    <name>${projectName}</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.soukuan</groupId>
        <artifactId>soukuan-microservice-parent</artifactId>
        <version>Edgware.SR3-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.plugin.version>3.6.1</maven.compiler.plugin.version>
        <maven.source.plugin.version>3.0.1</maven.source.plugin.version>
    </properties>

    <!--release版本发布 远程git仓库地址-->
    <scm>
        <connection>scm:git:http://git.wanshifu.com/service/wshifu-${projectName}.git</connection>
        <developerConnection>scm:git:http://git.wanshifu.com/service/wshifu-${projectName}.git</developerConnection>
        <url>scm:git:http://git.wanshifu.com/service/wshifu-${projectName}.git</url>
        <tag>HEAD</tag>
    </scm>

    <distributionManagement>
        <repository>
            <id>wanshifu-releases</id>
            <name>Nexus Release Repository</name>
            <url>http://nexus.wanshifu.com/repository/maven-releases/</url>
        </repository>
        <snapshotRepository>
            <id>wanshifu-snapshots</id>
            <name>Nexus Snapshot Repository</name>
            <url>http://nexus.wanshifu.com/repository/maven-snapshots/</url>
        </snapshotRepository>
    </distributionManagement>

    <build>
        <plugins>
            <!-- 要将源码放上去，需要加入这个插件 -->
            <plugin>
                <artifactId>maven-source-plugin</artifactId>
                <version>${r"${maven.source.plugin.version}"}</version>
                <configuration>
                    <attach>true</attach>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.codehaus.gmavenplus</groupId>
                <artifactId>gmavenplus-plugin</artifactId>
                <version>1.5</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <!--release版本发布插件-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-release-plugin</artifactId>
                <version>2.5.3</version>
                <configuration>
                    <autoVersionSubmodules>true</autoVersionSubmodules><!--自动更改所有子模块版本-->
                    <tagNameFormat>${project}-@{project.version}</tagNameFormat><!--生成的tag名字-->
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.apache.maven.scm</groupId>
                        <artifactId>maven-scm-provider-jgit</artifactId>
                        <version>1.9.5</version>
                    </dependency>
                </dependencies>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${r"${maven.compiler.plugin.version}"}</version>
                <configuration>
                    <source>${r"${java.version}"}</source>
                    <target>${r"${java.version}"}</target>
                </configuration>
            </plugin>
            <!--取消doc生成-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>2.9.1</version>
                <configuration>
                    <additionalparam>-Xdoclint:none</additionalparam>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
