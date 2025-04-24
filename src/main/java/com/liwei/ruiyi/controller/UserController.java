package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.TUserService;
import com.liwei.ruiyi.utils.PageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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



    @RequestMapping("/userManager")
    public String userManager() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        List<TDepartment> departments;
        if (user.getIsAdmin() == 1) {
            departments = userService.getDepartments();
        } else {
            departments = userService.getDepartmentsByCode(user.getDepartmentCode());
        }
        request.setAttribute("departments", departments);
        return "page/user/userManager";
    }

    @RequestMapping("/goCreateUserPage")
    public String goCreateUserPage() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        List<TDepartment> departments;
        if (user.getIsAdmin() == 1) {
            departments = userService.getDepartments();
        } else {
            departments = userService.getDepartmentsByCode(user.getDepartmentCode());
        }
        request.setAttribute("departments", departments);
        return "page/user/createUser";
    }

    @RequestMapping("/goUpdateUserPage")
    public String goUpdateUserPage(String id) {
        TUser queryUser = userService.queryUserById(id);
        request.setAttribute("queryUser", queryUser);

        TUser user = (TUser) request.getSession().getAttribute("user");
        List<TDepartment> departments;
        if (user.getIsAdmin() == 1) {
            departments = userService.getDepartments();
        } else {
            departments = userService.getDepartmentsByCode(user.getDepartmentCode());
        }
        request.setAttribute("departments", departments);
        return "page/user/updateUser";
    }

    @RequestMapping("/goUpdateOwnPage")
    public String goUpdateOwnPage() {
        TUser user = (TUser) request.getSession().getAttribute("user");
        TDepartment department = userService.getDepartmentById(user.getDepartmentId());
        request.setAttribute("department", department);

        return "page/user/updateOwnMessage";
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser(TUser user) {
        JSONObject rj = userService.addUser(user);
        return rj.toJSONString();
    }

    @RequestMapping("/checkUsername")
    @ResponseBody
    public String checkUsername(String username) {
        JSONObject rj = new JSONObject();
        boolean answer = userService.checkUsername(username);
        rj.put("status", answer);
        rj.put("msg", answer ? "用户名不存在，可以使用" : "用户名已存在，请修改");
        return rj.toJSONString();
    }


    @RequestMapping("/updateUserMessage")
    @ResponseBody
    public String updateUserMessage(TUser user) {
        boolean updateStatus = userService.updateUserMessage(user);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "用户信息修改成功。" : "用户信息修改失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/updateOwnMessage")
    @ResponseBody
    public String updateOwnMessage(TUser user) {
        TUser user1 = (TUser) request.getSession().getAttribute("user");
        user.setId(user1.getId());

        boolean updateStatus = userService.updateOwnMessage(user);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "信息修改成功。" : "信息修改失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/resetUserPassword")
    @ResponseBody
    public String resetUserPassword(String id, String password) {
        boolean updateStatus = userService.updateUserPassword(id, password);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "密码重置成功。" : "密码重置失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/updateLadder")
    @ResponseBody
    public String updateLadder(String id) {
        boolean updateStatus = userService.updateLadder(id);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "是否负责人状态更新成功。" : "是否负责人状态更新失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/updateAdmin")
    @ResponseBody
    public String updateAdmin(String id) {
        boolean updateStatus = userService.updateAdmin(id);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "是否管理员状态更新成功。" : "是否管理员状态更新失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/updateBan")
    @ResponseBody
    public String updateBan(String id) {
        boolean updateStatus = userService.updateBan(id);
        JSONObject rj = new JSONObject();
        rj.put("status", updateStatus);
        rj.put("msg", updateStatus ? "是否可以可以登录状态更新成功。" : "是否可以可以登录状态更新失败。");
        return rj.toJSONString();
    }

    @RequestMapping("/queryUser")
    @ResponseBody
    public String queryUser() {

        TUser user = (TUser) request.getSession().getAttribute("user");

        int nowPage = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageUtils pageUtils = new PageUtils(nowPage, limit);

        String key = request.getParameter("key");
        String departmentCode = request.getParameter("departmentCode");

        if(StringUtils.isEmpty(departmentCode) && user.getIsAdmin() != 1){
            departmentCode = user.getDepartmentCode();
        }

        JSONObject params = new JSONObject();
        params.put("key", key);
        params.put("departmentCode", departmentCode);
        pageUtils.setSearchParams(params);

        PageUtils page = userService.queryUserByPage(pageUtils);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("count", page.getTotal());
        returnJson.put("data", page.getData());
        return JSON.toJSONString(returnJson);
    }
}
