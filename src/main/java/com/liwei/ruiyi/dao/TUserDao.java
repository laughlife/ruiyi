package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TUserDao {
    TUser queryUserMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword,String password);

    List<TUser> getBmcyList(String code);
}
