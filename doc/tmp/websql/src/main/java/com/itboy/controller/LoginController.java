package com.itboy.controller;

import com.alibaba.fastjson.JSON;
import com.itboy.db.DbSourceFactory;
import com.itboy.model.*;
import com.itboy.service.LoginService;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description 登录控制
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 10:51
 **/
@Controller
@Log4j2
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private DbSourceFactory dbSourceFactory;

    @RequestMapping(value = {"/", "login", "index"}, method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("login");
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() == true) {
            SysUser user = (SysUser) currentUser.getPrincipal();
            mav.setViewName("index");
            mav.addObject("users", user);
        }
        return mav;
    }

    @RequestMapping("main")
    public String main() {
        return "main";
    }

    @RequestMapping("/userlogPage")
    public String userlogPage() {
        return "userlogPage";
    }

    @RequestMapping("/getHelpPage")
    public String getHelpPage() {
        return "helpPage";
    }


    @RequestMapping("/getUsersPage")
    public ModelAndView usersPage() {
        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mav = new ModelAndView("usersPage");
        mav.addObject("users", currentUser);
        return mav;
    }

    @RequestMapping("/login/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.out(4, request, response);
    }

    @RequestMapping("/getSysSetUpPage")
    public ModelAndView getSysSetUpPage() {
        ModelAndView mav = new ModelAndView("sysSetUpPage");
        SysSetup sysSetup = dbSourceFactory.getSysSetUp();
        mav.addObject("obj", sysSetup);
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(Map<String, Object> map, HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName").trim().toLowerCase();
        String password = request.getParameter("password").trim().toLowerCase();
        String captcha = request.getParameter("captcha").trim().toLowerCase();
        if (!CaptchaUtil.ver(captcha, request)) {
            CaptchaUtil.clear(request);
            map.put("msg", "验证码不正确！");
            map.put("userName", userName);
            map.put("logins", 2);
            return JSON.toJSONString(map);
        }
        LoginResult loginResult = loginService.login(userName, password, request);
        if (!loginResult.isLogin()) {
            map.put("msg", loginResult.getResult());
            map.put("userName", userName);
            map.put("logins", 2);
        } else {
            map.put("logins", 1);
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/logout")
    public String logOut(HttpSession session) {
        loginService.logout();
        return "/user/login";
    }

    @RequestMapping("getUserLogList")
    @ResponseBody
    public Map getLogList(SysUserLog model) {
        Map result = new HashMap();
        Result<SysUserLog> p = loginService.getLogList(model);
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }

    @RequestMapping("userController/updateUsers")
    @ResponseBody
    public String updateUsers(SysUser sysUser) {
        Map<String, Object> result = new HashMap<>();
        try {
            loginService.updateUsers(sysUser);
            result.put("code", 1);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("code", 2);
            result.put("msg", e.getMessage());
        }
        return JSON.toJSONString(result);
    }

    //修改设置保存 回家熬不住了~~
    @RequestMapping("sysConroller/updateSysSetUp")
    @ResponseBody
    public String updateSysSetUp(SysSetup sys) {
        Map<String, Object> result = new HashMap<>();
        try {
            loginService.updateSysSetUp(sys);
            result.put("code", 1);
        } catch (Exception e) {
            result.put("code", 2);
            result.put("msg", e.getMessage());
            log.error(e.getMessage());
        }
        return JSON.toJSONString(result);
    }


}
