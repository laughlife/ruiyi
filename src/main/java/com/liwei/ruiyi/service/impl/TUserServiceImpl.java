package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.dao.TDepartmentDao;
import com.liwei.ruiyi.dao.TUserDao;
import com.liwei.ruiyi.service.TUserService;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userService")
public class TUserServiceImpl implements TUserService {

    @Autowired
    TUserDao userDao;

    @Autowired
    TDepartmentDao departmentDao;
    @Override
    public TUser queryUserMessage(String username) {
        return userDao.queryUserMessage(username);
    }

    @Override
    public boolean updatePwd(Integer id, String oldPassword, String password) {
        return userDao.updatePwd(id, oldPassword, password);
    }

    @Override
    public PageUtils queryUserByPage(PageUtils page) {
        PageUtils userPage = userDao.queryUsers(page);
        List<TUser> data = userPage.getData();
        List<JSONObject> userList = new ArrayList<>();
        for (TUser user : data) {
            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put("username", user.getUsername());
            userJson.put("name", user.getName());
            userJson.put("phone", user.getPhone());
            userJson.put("department", user.getDepartmentName());
            if(user.getIsAdmin() == 1){
                userJson.put("is_admin", "是");
            }else{
                userJson.put("is_admin", "否");
            }
            if(user.getIsLadder() == 1){
                userJson.put("is_ladder", "是");
            }else{
                userJson.put("is_ladder", "否");
            }
            if(user.getIsBan() == 1){
                userJson.put("is_ban", "否");
            }else{
                userJson.put("is_ban", "是");
            }
            userList.add(userJson);
        }
        userPage.setData(userList);
        return userPage;
    }

    @Override
    public List<TDepartment> getDepartments() {
        return departmentDao.getBmList();
    }

    @Override
    public boolean checkUsername(String username) {
        return userDao.checkUsername(username);
    }

    @Override
    public JSONObject addUser(TUser user) {
        JSONObject rj = new JSONObject();
        if(userDao.checkUsername(user.getUsername())){
            boolean result = userDao.addUser(user);
            rj.put("status", result);
            rj.put("msg", result ? "添加成功" : "添加失败，请查找原因");
        }else{
            rj.put("status", false);
            rj.put("msg", "用户名已存在");
        }
        return rj;
    }

    @Override
    public TUser queryUserById(String id) {
        return userDao.queryUserById(id);
    }

    @Override
    public boolean updateUserMessage(TUser user) {
        return userDao.updateUserMessage(user);
    }

    @Override
    public boolean updateOwnMessage(TUser user) {
        return userDao.updateOwnMessage(user);
    }

    @Override
    public TDepartment getDepartmentById(Integer departmentId) {
        return userDao.getDepartmentById(departmentId);
    }

    @Override
    public boolean updateUserPassword(String id, String password) {
        return userDao.updateUserPassword(id, password);
    }

    @Override
    public boolean updateLadder(String id) {
        return userDao.updateLadder(id);
    }

    @Override
    public boolean updateAdmin(String id) {
        return userDao.updateAdmin(id);
    }

    @Override
    public boolean updateBan(String id) {
        return userDao.updateBan(id);
    }

    @Override
    public List<TDepartment> getDepartmentsByCode(String departmentCode) {
        return departmentDao.getDepartmentsByCode(departmentCode);
    }
}
