package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.bo.mapper.TUserMapper;
import com.liwei.ruiyi.dao.TUserDao;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    @Override
    public List<TUser> getBmcyList(String code) {
        return List.of();
    }

    @Override
    public PageUtils queryUsers(PageUtils page) {
        JSONObject searchParams = page.getSearchParams();
        String departmentCode = searchParams.getString("departmentCode");
        String key = searchParams.getString("key");
        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from t_user where 1 = ?";
        String querySql = "select * from t_user where 1 = ? ";
        List<Object> args = new ArrayList<>();
        args.add(1);
        if (StringUtils.isNotBlank(key)) {
            key = "%" + key.trim() + "%";
            sql += " and (username like ? or name like ?)";
            querySql += " and (username like ? or name like ?)";

            args.add(key);
            args.add(key);
        }

        if (StringUtils.isNotBlank(departmentCode)) {
            sql += " and department_code like ?";
            querySql += " and department_code like ?";
            args.add(departmentCode + "%");
        }

        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);

        querySql += " limit ?,?";
        args.add(pageStart);
        args.add(limit);

        List<TUser> userList = jdbc.query(querySql, new TUserMapper(), args.toArray());
        page.setData(userList);

        return page;
    }

    @Override
    public boolean checkUsername(String username) {
        String sql = "select count(0) from t_user where username = ?";
        int count = jdbc.queryForObject(sql, Integer.class, username);
        return count == 0;
    }

    @Override
    public boolean addUser(TUser user) {
        Integer department_id  = user.getDepartmentId();
        String sql = "select code from t_department where id = ?";
        String departmentCode = jdbc.queryForObject(sql, String.class, department_id);
        sql = "insert into t_user(username,password,name,phone,department_id," +
                "department_code,is_ladder,is_admin,create_time,delete_time," +
                "is_ban) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?)";
        Object[] args = {user.getUsername(), user.getPassword(), user.getName(), user.getPhone(), department_id,
                departmentCode, 0, 0, DateUtils.getSystemTime(), null,
                0};
        return false;
    }
}
