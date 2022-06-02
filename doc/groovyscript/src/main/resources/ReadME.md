### 使用方法
1. 在Maven Pom 文件中添加依赖即可使用
```xml
        <dependency>
            <groupId>com.wauil</groupId>
            <artifactId>groovy-spring-boot-starter</artifactId>
            <version>${wauil-groovy-version}</version>
        </dependency>
```
2. 如添加依赖后不想启用该功能，则配置wauil.groovy.enabled值为false；
3. 调用接口 POST /groovy 传入groovy脚本即可执行；
