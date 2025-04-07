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


    @Autowired
    HttpServletRequest request;

    @RequestMapping("/userLogin")
    @ResponseBody
    public String userLogin(String username, String password) {
        JSONObject rj = new JSONObject();
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            TUser user = userService.queryUserMessage(username, password);
            if (null != user) {
                rj.put("status", "success");
                rj.put("user", user);
                session.setAttribute("user", user);
            } else {
                rj.put("status", "fail");
                rj.put("message", "请检查用户名或密码");
            }
        } else {
            rj.put("status", "fail");
            rj.put("message", "请检查用户名或密码");
        }
        return rj.toJSONString();
    }

    @RequestMapping("/home")
    public String home() {
        return "main";
    }


    private Map<String, Object> createSeries(String name, int size, Random random) {
        Map<String, Object> series = new HashMap<>();
        series.put("name", name);
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(random.nextInt(500));
        }
        series.put("data", data);
        return series;
    }
}
