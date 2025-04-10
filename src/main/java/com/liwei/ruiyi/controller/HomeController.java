package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.bo.TUser;
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
        TUser user = (TUser) request.getSession().getAttribute("user");
        JSONObject json = permissionService.getPermissionsByDepartmentId(user.getDepartmentId());
        request.setAttribute("menuList", json.getJSONArray("data"));

        return "home";
    }

    @RequestMapping("/initMenu")
    @ResponseBody
    public String initMenu() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        JSONObject json = permissionService.getPermissionsByDepartmentId(user.getDepartmentId());
        return json.toJSONString();
    }

    @RequestMapping("/addRootMenu")
    @ResponseBody
    public String addRootMenu(String name) {
        JSONObject json = new JSONObject();
        boolean isSuccess = permissionService.addRootMenu(name);
        json.put("status", isSuccess);
        json.put("msg", isSuccess?"添加成功，页面会自动刷新，请稍后。":"添加失败，请联系开发人员查找失败原因。");
        return json.toJSONString();
    }

    @RequestMapping("/addChildMenu")
    @ResponseBody
    public String addChildMenu(String parentId,String name) {
        JSONObject json = new JSONObject();
        boolean isSuccess = permissionService.addChildMenu(parentId, name);
        json.put("status", isSuccess);
        json.put("msg", isSuccess?"添加成功，页面会自动刷新，请稍后。":"添加失败，请联系开发人员查找失败原因。");
        return json.toJSONString();
    }
    @RequestMapping("/updatePermission")
    @ResponseBody
    public String updatePermission(String id,String field,String value) {
        JSONObject json = new JSONObject();
        boolean isSuccess = permissionService.updatePermission(id, field, value);
        json.put("status", isSuccess);
        json.put("msg", isSuccess?"数据修改成功。":"数据修改失败，请联系开发人员查找失败原因。");
        return json.toJSONString();
    }

    @RequestMapping("/deletePermission")
    @ResponseBody
    public String deletePermission(String id) {
        JSONObject json = new JSONObject();
        boolean isSuccess = permissionService.deletePermission(id);
        json.put("status", isSuccess);
        json.put("msg", isSuccess?"数据删除成功。":"数据删除失败，请确认是否包含有子级节点。");
        return json.toJSONString();
    }
}
