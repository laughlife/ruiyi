package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.bo.mapper.TUserMapper;
import com.liwei.ruiyi.dao.TUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDao")
public class TUserDaoImpl implements TUserDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public TUser queryUserMessage(String username, String password) {
        String sql = "select * from t_user where username = ? and password = ?";
        Object[] params = {username, password};
        List<TUser> adminList = jdbc.query(sql, new TUserMapper(), params);
        TUser admin = null;
        if (adminList.size() > 0) {
            admin = adminList.get(0);
            return admin;
        }
        return null;
    }

    @Override
    public boolean updatePwd(Integer id, String oldPassword, String password) {
        String sql = "update t_user set password = ? where id = ? and password = ?";
        Object[] params = {password, id, oldPassword};
        int count = jdbc.update(sql, params);
        return count > 0;
    }
}
