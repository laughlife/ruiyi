package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TUserDao {
    TUser queryUserMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword,String password);

    List<TUser> getBmcyList(String code);

    PageUtils queryUsers(PageUtils page);

    boolean checkUsername(String username);

    boolean addUser(TUser user);

    TUser queryUserById(String id);

    boolean updateUserMessage(TUser user);

    boolean updateOwnMessage(TUser user);

    TDepartment getDepartmentById(Integer departmentId);

    boolean updateUserPassword(String id, String password);

    boolean updateLadder(String id);

    boolean updateAdmin(String id);

    boolean updateBan(String id);
}
