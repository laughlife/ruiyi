package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    PermissionService permissionService;

    @RequestMapping("/permissionManager")
    public String userManager() {
        return "page/permission/permissionManager";
    }

    @RequestMapping("/initDepartmentTree")
    @ResponseBody
    public String initDepartmentTree() {
        JSONArray ja = permissionService.getAllDepartments();
        JSONObject jo = new JSONObject();
        TUser user = (TUser) request.getSession().getAttribute("user");
        if (user.getIsAdmin() == 1) {
            jo.put("status", true);
            jo.put("data", ja);
        }
        return jo.toJSONString();
    }

    @RequestMapping("/getPermissionTree")
    public String getPermissionTree(String departmentId) {
        TUser user = (TUser) request.getSession().getAttribute("user");
        JSONArray ja = permissionService.getPermissionByDepartmentId(departmentId,user.getIsAdmin());
        return "";
    }

}
