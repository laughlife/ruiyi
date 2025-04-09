package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.TUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    public TUserService userService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/userLogin")
    @ResponseBody
    public String userLogin(String username, String password) {
        JSONObject rj = new JSONObject();
        rj.put("status", false);
        rj.put("msg", "请检查用户名或密码");
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            //首先查询用户是否存在，如果存在了返回用户信息，
            // 接着再确定用户是否被禁用，如果被禁用，也是无法登录的，直接返回登录失败。
            TUser user = userService.queryUserMessage(username, password);
            if (null != user && user.getIsBan() == 0) {
                rj.put("status", true);
                rj.put("msg", "登录成功。");
                session.setAttribute("user", user);
            }else{
                rj.put("status", false);
                rj.put("msg", "用户已被禁止登录，请联系管理员或负责人。");
            }
        }
        return rj.toJSONString();
    }

    @RequestMapping("/home")
    public String home() {
        return "main";
    }

}
