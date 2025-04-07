package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.dao.TUserDao;
import com.liwei.ruiyi.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userService")
public class TUserServiceImpl implements TUserService {

    @Autowired
    TUserDao userDao;

    @Override
    public TUser queryUserMessage(String username, String password) {
        return userDao.queryUserMessage(username, password);
    }

    @Override
    public boolean updatePwd(Integer id, String oldPassword, String password) {
        return userDao.updatePwd(id, oldPassword, password);
    }
}
