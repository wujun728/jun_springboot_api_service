# mybatis-groovy

## TOC

[[toc]]

#### Introduction

Write Sql In Groovy Script.

See the [中文文档](https://github.com/TaylorXian/mybatis-groovy/blob/master/README-zh.md) for Chinese README.

#### Design


#### Install


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

#### Usage

##### Groovy Method

- Mapper class: [UserMapper.java](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/src/main/java/org/harmony/toddler/mybatis/spring/demo/mapper/UserMapper.java)
```java
@Mapper
public interface UserMapper {

    @Select("UserMapperSql#selectByCondition")
    @Lang(GroovyLangDriver.class)
    List<UserVO> selectByCondition(UserVO userVO);

}
```
- Script: [UserMapperSql.groovy](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/sql/UserMapperSql.groovy)
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

- Mapper class: [UserMapper.java](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/src/main/java/org/harmony/toddler/mybatis/spring/demo/mapper/UserMapper.java)
```java
@Mapper
public interface UserMapper {

    @Select("UserMapperSelectByName")
    @Lang(GroovyLangDriver.class)
    UserVO selectByName(String name);

}
```
- Script: [UserMapperSelectByName.groovy](https://github.com/TaylorXian/mybatis-groovy/blob/master/mybatis-groovy-parent/spring-boot-demo/sql/UserMapperSelectByName.groovy)
```groovy
'''select * from user where name = #{name}'''
```

#### Contribution


