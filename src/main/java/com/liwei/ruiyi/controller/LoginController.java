package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.TUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @RequestMapping("/userLogin")
    @ResponseBody
    public String userLogin(String username, String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        JSONObject rj = new JSONObject();

        try {
            // 1. 尝试认证
            UsernamePasswordAuthenticationToken authReq =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authReq);

            // 2. 保存安全上下文
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 3. 记住我（如果勾选）
            if ("on".equals(request.getParameter("remember-me"))) {
                rememberMeServices.loginSuccess(request, response, auth);
            }

            // 4. 返回前端 JSON
            rj.put("status", true);
            rj.put("icon", 1);
            rj.put("msg", "登录成功。");
        } catch (AuthenticationException ex) {
            rj.put("status", false);
            rj.put("icon", 2);
            rj.put("msg", "登录失败");
        }
        return rj.toJSONString();
    }

    @RequestMapping("/home")
    public String home() {
        return "main";
    }

}
