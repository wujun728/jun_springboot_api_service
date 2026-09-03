package com.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.demo.entity.Student;
import com.gitee.freakchicken.DBAPI;
import com.gitee.freakchicken.entity.SqlWithParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/student")
@RestController
public class HomeController {

    @Autowired
    DBAPI dbapi;

    @RequestMapping("/getAll")
    public List<JSONObject> getAllStudent() {
        List<JSONObject> jsonObjects = dbapi.executeQuery("user", "getAllUser");
        return jsonObjects;
    }

    @RequestMapping("/getUserById")
    public List<JSONObject> getStudentById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<JSONObject> jsonObjects = dbapi.executeQuery("user", "getUserById", map);
        return jsonObjects;
    }

    @RequestMapping("/getUserById/entity")
    public List<Student> getStudentById2(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<Student> list = dbapi.executeQueryEntity("user", "getUserById", map, Student.class);
        return list;
    }

    @RequestMapping("/add")
    public Integer add(String name, Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        int i = dbapi.executeUpdate("user", "createStudent", map);
        return i;
    }

    @RequestMapping("/deleteAll")
    public Integer deleteAll(String name, Integer age) {
        int i = dbapi.executeUpdate("user", "deleteAll");
        return i;
    }

    @RequestMapping("/getAllLog")
    public List<JSONObject> getAllLog() {
        List<JSONObject> list = dbapi.executeQuery("log", "getAllLog");
        return list;
    }

    @RequestMapping("/getAllLog2")
    public Object getAllLog2() {
        Object object = dbapi.execute("log", "getAllLog");
        return object;
    }

    @RequestMapping("/transaction")
    public void testTransaction(@RequestParam Map<String, Object> map) {

        List<SqlWithParam> list = new ArrayList<>();

        SqlWithParam sqlWithParam = new SqlWithParam().setNamespace("user").setSqlId("deleteAll").setParameters(map);
        SqlWithParam sqlWithParam2 = new SqlWithParam().setNamespace("user").setSqlId("createStudent").setParameters(map);

        list.add(sqlWithParam);
        list.add(sqlWithParam2);
        List<Object> result = dbapi.executeWithinTransaction(list);

        System.out.println(result);
    }

}
