package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TUserService {
    TUser queryUserMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword,String password);

    PageUtils queryUserByPage(PageUtils page);

    List<TDepartment> getDepartments();

    boolean checkUsername(String username);

    JSONObject addUser(TUser user);
}
