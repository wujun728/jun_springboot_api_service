<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>${basePackage}</groupId>
        <artifactId>${projectName}</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>${projectName}-web</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <java.version>1.8</java.version>
        <wanshfiu-framework-version>1.0.0-SNAPSHOT</wanshfiu-framework-version>
        <wanshfiu-api-version>1.0.10-SNAPSHOT</wanshfiu-api-version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.soukuan</groupId>
            <artifactId>soukuan-microservice-cloud-denpendencies</artifactId>
        </dependency>

        <dependency>
            <groupId>com.soukuan</groupId>
            <artifactId>soukuan-framework-all-spring-boot-starter</artifactId>
            <version>${r"${wanshfiu-framework-version}"}</version>
        </dependency>

        <dependency>
            <groupId>com.soukuan</groupId>
            <artifactId>soukuan-framework-test</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.soukuan</groupId>
            <artifactId>${projectName}-api</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

    </dependencies>

    <!--多环境部署配置-->
    <profiles>
        <profile>
            <id>test</id>
            <properties>
                <deploy.env>test</deploy.env>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <deploy.env>prod</deploy.env>
            </properties>
        </profile>
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <deploy.env>dev</deploy.env>
            </properties>
        </profile>
    </profiles>

    <build>
        <filters>
            <filter>src/main/resources/config/env/${r"${deploy.env}"}-env.properties</filter>
        </filters>
        <resources>
            <resource>
                <directory>src/main/resources/</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <plugins>
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
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <finalName>${projectName}</finalName>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>