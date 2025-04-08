package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DepartmentService departmentService;


    @RequestMapping("/queryAllBm")
    @ResponseBody
    public String queryAllBm() {
        JSONObject bmList = departmentService.queryAllBm();
        return bmList.toJSONString();
    }

    @RequestMapping("/goCreateBmPage")
    public String goCreateBmPage() {
        String parentId = request.getParameter("parentId");
        if(StringUtils.isNotBlank(parentId)){
            TDepartment parentBm = departmentService.getBmById(parentId);
            request.setAttribute("sjbm", parentBm);
        }
        return "page/bm/createBm";
    }

    @RequestMapping("/addBm")
    @ResponseBody
    public String addBm(TDepartment bm) {
        //添加部门信息
        boolean isSave = departmentService.addBm(bm);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", isSave ? "success" : "file");
        returnJson.put("message", isSave ? "部门信息保存成功。" : "部门信息保存失败，请查找原因。");
        return JSON.toJSONString(returnJson);
    }


    @RequestMapping("/goAddBmcyPage")
    public String goAddBmcyPage(String id) {
        TDepartment bm = departmentService.getBmById(id);
        request.setAttribute("bm", bm);

        JSONArray userArray = departmentService.queryBmcy(id);
        request.setAttribute("userList", userArray);

        request.setAttribute("id", id);

        JSONArray queryUser = departmentService.queryBmcyByKey(id, "");
        request.setAttribute("queryUser", queryUser);

        return "page/bm/addBmcy";
    }

    @RequestMapping("/updateBm")
    @ResponseBody
    public String updateBm() {
        String id = request.getParameter("id");
        String field = request.getParameter("field");
        String value = request.getParameter("value");
        JSONObject params = new JSONObject();
        params.put("id", id);
        params.put("field", field);
        params.put("value", value);
        boolean isSave = departmentService.updateBm(params);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", isSave);
        returnJson.put("message", isSave ? "部门信息保存成功。" : "部门信息保存失败，请查找原因。");
        return JSON.toJSONString(returnJson);
    }


    @RequestMapping("/queryAllBmcy")
    @ResponseBody
    public String queryAllBmcy() {
        String id = request.getParameter("id");
        Integer idInt = Integer.parseInt(id);
        List<TUser> bmcyList = departmentService.getBmcyList(idInt);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("count", bmcyList.isEmpty() ? 0 : bmcyList.size());
        returnJson.put("data", bmcyList);
        return JSON.toJSONString(returnJson);
    }

    @RequestMapping("/searchUser")
    @ResponseBody
    public String searchUser(String id, String key) {
        System.out.println(id);
        System.out.println(key);
        JSONArray userArray = departmentService.queryBmcyByKey(id, key);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("users", userArray);
        return returnJson.toJSONString();
    }

    @RequestMapping("/updateUsers")
    @ResponseBody
    public String updateUsers(@RequestParam(value = "newUserIds", required = false, defaultValue = "") String[] newUserIds,
                              @RequestParam(value = "removedUserIds", required = false, defaultValue = "") String[] removedUserIds,
                              @RequestParam("id") String id) {
        boolean updated = departmentService.updateBmcy(newUserIds, removedUserIds, id);

        JSONObject returnJson = new JSONObject();
        returnJson.put("status", updated);
        returnJson.put("message", updated ? "操作成功" : "数据没有发生改变，请查找原因。");
        return returnJson.toJSONString();
    }

    @RequestMapping("/deleteBm")
    @ResponseBody
    public String deleteBm(String id) {
        boolean isDelete = departmentService.deleteBm(id);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", isDelete ? "success" : "file");
        returnJson.put("message", isDelete ? "部门信息删除成功。" : "部门信息删除失败，请查找原因。");
        return JSON.toJSONString(returnJson);
    }

    @RequestMapping("/deleteBmcy")
    @ResponseBody
    public String deleteBmcy(String id) {
        boolean isDelete = departmentService.deleteBmcyById(id);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", isDelete ? "success" : "file");
        returnJson.put("message", isDelete ? "部门信息删除成功。" : "部门信息删除失败，请查找原因。");
        return JSON.toJSONString(returnJson);
    }
    @RequestMapping("/setld")
    @ResponseBody
    public String setld(String id) {
        boolean isDelete = departmentService.setLdById(id);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", isDelete ? "success" : "file");
        returnJson.put("message", isDelete ? "部门信息删除成功。" : "部门信息删除失败，请查找原因。");
        return JSON.toJSONString(returnJson);
    }
}
