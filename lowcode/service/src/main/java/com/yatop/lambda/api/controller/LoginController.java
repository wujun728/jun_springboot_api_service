package com.yatop.lambda.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap)
    {
        return "login";
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response, ModelMap mmap){
        return "test";
    }

}
