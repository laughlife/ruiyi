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
import org.springframework.security.core.authority.AuthorityUtils;
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
    HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    TUserService userService;

    @RequestMapping("/userLogin")
    @ResponseBody
    public String userLogin(String username, String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        JSONObject rj = new JSONObject();
        try {
            // 1. 尝试认证
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authReq);
            // 2. 保存安全上下文
            SecurityContextHolder.getContext().setAuthentication(auth);
            // 3. 记住我（如果勾选）
            if ("on".equals(request.getParameter("remember-me"))) {
                rememberMeServices.loginSuccess(request, response, auth);
            }

            TUser user = userService.queryUserMessage(username);
            request.getSession().setAttribute("user", user);

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

    @RequestMapping("/loginByToken")
    public String loginByToken(String token, HttpServletRequest request) {
        if (token != null) {
            // 根据 token 从数据库中获取用户信息
            TUser user = userService.findUserByToken(token);

            if (user != null) {
                // 手动登录成功
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.getSession().setAttribute("user", user); // 如果需要存储到 Session
                return "home"; // 跳转到主页
            }
        }

        return "index"; // 如果没有找到 token 或 token 无效，跳转到登录页
    }

    @RequestMapping("/loginout")
    public String loginout() {
        // 清理会话
        request.getSession().invalidate();
        return "index";
    }

    @RequestMapping("/home")
    public String home() {
        return "main";
    }

}
