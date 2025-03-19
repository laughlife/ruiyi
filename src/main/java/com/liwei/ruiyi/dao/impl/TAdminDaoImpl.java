package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TAdmin;
import com.liwei.ruiyi.bo.mapper.TAdminRowMapper;
import com.liwei.ruiyi.dao.TAdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("adminDao")
public class TAdminDaoImpl implements TAdminDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public TAdmin queryAdminMessage(String username, String password) {
        String sql = "select * from t_admin where username = ? and password = ?";
        Object[] params = {username, password};
        List<TAdmin> adminList = jdbc.query(sql, new TAdminRowMapper(), params);
        TAdmin admin = null;
        if (adminList.size() > 0) {
            admin = adminList.get(0);
            return admin;
        }
        return null;
    }

    @Override
    public boolean updatePwd(Integer id, String oldPassword, String password) {
        String sql = "update t_admin set password = ? where id = ? and password = ?";
        Object[] params = {password, id, oldPassword};
        int count = jdbc.update(sql, params);
        return count > 0;
    }
}