package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.TUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    TUserService userService;

    @RequestMapping("/updatePwd")
    @ResponseBody
    public String updatePwd() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        boolean update = userService.updatePwd(user.getId(), request.getParameter("oldPassword"), request.getParameter("password"));
        JSONObject rj = new JSONObject();
        rj.put("status", update);
        rj.put("message", update ? "修改成功" : "修改失败，请查找原因");
        return rj.toString();
    }

    @RequestMapping("/loginout")
    public String loginout() {
        // 清理会话
        request.getSession().invalidate();
        return "index";
    }
    @RequestMapping("/setting")
    public String setting() {
        // 清理会话
        request.getSession().invalidate();
        return "page/user/setting";
    }
}
