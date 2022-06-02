# mybatis-groovy

## TOC

[[toc]]

#### 简介

用 Groovy 脚本写 Sql。

英文说明 [README](https://github.com/TaylorXian/mybatis-groovy/blob/master/README.md).

#### 设计


#### 安装


##### Spring boot
- Maven
```xml
<dependency>
    <groupId>io.github.taylorxian</groupId>
    <artifactId>mybatis-groovy-boot-starter</artifactId>
    <version>1.0-RC</version>
</dependency>
```
##### Java App
- Maven
```xml
<dependency>
    <groupId>io.github.taylorxian</groupId>
    <artifactId>mybatis-groovy</artifactId>
    <version>1.0-RC</version>
</dependency>
```

#### 使用说明

##### Groovy 方法

- Mapper 类: [UserMapper.java](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/src/main/java/org/harmony/toddler/mybatis/spring/demo/mapper/UserMapper.java)
```java
@Mapper
public interface UserMapper {

    @Select("UserMapperSql#selectByCondition")
    @Lang(GroovyLangDriver.class)
    List<UserVO> selectByCondition(UserVO userVO);

}
```
- 脚本: [UserMapperSql.groovy](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/sql/UserMapperSql.groovy)
```groovy
def selectByCondition(Object parameter) {
    '''
SELECT id, name, age, addr
FROM `user`
''' + where { c ->
        if (parameter?.id) c += 'id = #{id} '
        if (parameter?.name) c += ' AND name = #{name} '
        c
    }
}
```

##### Groovy Script

- Mapper 类: [UserMapper.java](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/src/main/java/org/harmony/toddler/mybatis/spring/demo/mapper/UserMapper.java)
```java
@Mapper
public interface UserMapper {

    @Select("UserMapperSelectByName")
    @Lang(GroovyLangDriver.class)
    UserVO selectByName(String name);

}
```
- 脚本: [UserMapperSelectByName.groovy](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/sql/UserMapperSelectByName.groovy)
```groovy
'''select * from user where name = #{name}'''
```

#### 贡献


