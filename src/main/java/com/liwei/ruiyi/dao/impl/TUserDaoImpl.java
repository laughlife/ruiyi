package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.bo.mapper.TDepartmentMapper;
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
        String sql = "select * from t_user where department_code like ?";
        List<TUser> userList = jdbc.query("select * from t_user where department_code like ?", new TUserMapper(), code + "%");
        if (userList.size() > 0) {
            return userList;
        }
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
        Integer department_id = user.getDepartmentId();
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
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public TUser queryUserById(String id) {
        String sql = "select * from t_user where id = ?";
        List<TUser> userList = jdbc.query(sql, new TUserMapper(), id);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public boolean updateUserMessage(TUser user) {
        Integer department_id = user.getDepartmentId();
        String sql = "select code from t_department where id = ?";
        String departmentCode = jdbc.queryForObject(sql, String.class, department_id);

        sql = "update t_user set username=?,name = ?,phone = ?,department_code=?,department_id=? where id = ?";
        Object[] args = {user.getUsername(), user.getName(), user.getPhone(), departmentCode, department_id, user.getId()};
        int count = 0;
        try {
            //这里添加try catch是因为这里有可能会出现索引冲突
            count = jdbc.update(sql, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count > 0;
    }

    @Override
    public boolean updateOwnMessage(TUser user) {
        String sql = "update t_user set name = ?,phone = ? where id = ?";
        Object[] args = {user.getName(), user.getPhone(), user.getId()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public TDepartment getDepartmentById(Integer departmentId) {
        String sql = "select * from t_department where id = ?";
        List<TDepartment> departmentList = jdbc.query(sql, new TDepartmentMapper(), departmentId);
        if (departmentList.size() > 0) {
            return departmentList.get(0);
        }
        return null;
    }

    @Override
    public boolean updateUserPassword(String id, String password) {
        String sql = "update t_user set password = ? where id = ?";
        int count = jdbc.update(sql, password, id);
        return count > 0;
    }

    @Override
    public boolean updateLadder(String id) {
        String sql = "update t_user set is_ladder = CASE WHEN is_ladder = 0 THEN 1 ELSE 0 END where id = ?";
        int count = jdbc.update(sql, id);
        return count > 0;
    }

    @Override
    public boolean updateAdmin(String id) {
        String sql = "update t_user set is_admin = CASE WHEN is_admin = 0 THEN 1 ELSE 0 END where id = ?";
        int count = jdbc.update(sql, id);
        return count > 0;
    }

    @Override
    public boolean updateBan(String id) {
        String sql = "update t_user set is_ban = CASE WHEN is_ban = 0 THEN 1 ELSE 0 END where id = ?";
        int count = jdbc.update(sql, id);
        if (count > 0) {
            sql = "update t_user set delete_time = CASE WHEN is_ban = 0 THEN null ELSE ? END where id = ?";
            count = jdbc.update(sql, DateUtils.getSystemTime(), id);
        }
        return count > 0;
    }
}
