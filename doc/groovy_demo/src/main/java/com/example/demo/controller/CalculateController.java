package com.example.demo.controller;

import com.example.demo.groovy.calculate.GroovyParserEngine;
import com.example.demo.groovy.core.GroovyDynamicLoader;
import groovy.lang.GroovyShell;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
public class CalculateController {

    @Resource
    private GroovyParserEngine groovyParserEngine;

    @Resource
    private GroovyDynamicLoader groovyDynamicLoader;

    @RequestMapping("/calculate")
    public int calculate(int number1, int number2) {

        return number1 + number2;

        /*String interfaceId = "B.integration.A.calculate.reward";

        Map<String, Object> map = new HashMap<>();
        map.put("totalAmount", "10");
        map.put("refererNumber", "5");

        request.setInterfaceId(interfaceId);
        request.setExtendInfo(map);

        return groovyParserEngine.parse(request);*/
    }

    @RequestMapping("/refresh")
    public void refresh() {
        groovyDynamicLoader.refresh();
    }

    public static void main(String[] args) throws IOException {
        GroovyShell groovyShell = new GroovyShell();
        Object evaluate = groovyShell.evaluate(new File("src/main/java/com/example/demo/groovy/Test.groovy"));
        System.out.println(evaluate.toString());
    }
}
