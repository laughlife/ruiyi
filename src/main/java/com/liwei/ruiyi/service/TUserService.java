package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TUserService {
    TUser queryUserMessage(String username);

    boolean updatePwd(Integer id, String oldPassword,String password);

    PageUtils queryUserByPage(PageUtils page);

    List<TDepartment> getDepartments();

    boolean checkUsername(String username);

    JSONObject addUser(TUser user);

    TUser queryUserById(String id);

    boolean updateUserMessage(TUser user);

    boolean updateOwnMessage(TUser user);

    TDepartment getDepartmentById(Integer departmentId);

    boolean updateUserPassword(String id, String password);

    boolean updateLadder(String id);

    boolean updateAdmin(String id);

    boolean updateBan(String id);

    List<TDepartment> getDepartmentsByCode(String departmentCode);
}
