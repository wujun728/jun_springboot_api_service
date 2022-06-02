package org.harmony.toddler.mybatis.groovy.demo.mapper;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.harmony.toddler.mybatis.groovy.GroovyLangDriver;
import org.harmony.toddler.mybatis.groovy.demo.domain.User;

public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User selectByPrimaryKey(int id);

    User findByIdXml(int id);

    @Select("select * from user where name = ${condition}")
    User findByCondition(String condition);

    User findByName(String name);

    @Select("UserMapperSql#selectById")
    @Lang(GroovyLangDriver.class)
    User selectById(int id);

    @Select("UserMapperSql#selectByWrapper")
    @Lang(GroovyLangDriver.class)
    User selectByWrapper(@Param("id") int id, @Param("name") String name);
}