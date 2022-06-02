package org.harmony.toddler.mybatis.groovy.demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Integer id;

    private String name;

    private Integer age;

    private String addr;

    private Date createTime;

    private Date updateTime;

}