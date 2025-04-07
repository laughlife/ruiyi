package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    PermissionService permissionService;

    @RequestMapping("/goHomePage")
    public String goHomePage() {
        String dev = request.getParameter("dev");
        if(StringUtils.isNotBlank(dev)){
            request.getSession().setAttribute("dev", dev);
        }else{
            request.getSession().removeAttribute("dev");
        }

        JSONArray array = permissionService.getAllPermission();
        request.setAttribute("permissionList", array);
        return "home";
    }
}
