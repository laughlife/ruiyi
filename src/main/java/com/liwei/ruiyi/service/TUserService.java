package com.liwei.ruiyi.service;

import com.liwei.ruiyi.bo.TUser;
import org.springframework.stereotype.Service;

@Service
public interface TUserService {
    TUser queryUserMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword,String password);
}
