package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CucumberController {

    /**
     * 判断今天是否是周五
     *
     * @param today
     * @return
     */
    @RequestMapping("/isFriday")
    public boolean isFriday(String today) {

        return "Friday".equalsIgnoreCase(today);
    }
}
