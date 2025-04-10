package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.bo.mapper.TPermissionMapper;
import com.liwei.ruiyi.dao.TPermissionDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository("permissionDao")
public class TPermissionDaoImpl implements TPermissionDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<TPermission> getAllPermission() {
        String sql = "select * from t_permission";
        return jdbc.query(sql, new TPermissionMapper());
    }

    @Override
    public List<TPermission> getPermissionsByDepartmentId(Integer departmentId) {
        String sql = "select * from t_permission where id in (select permission_id from t_department_permission where department_id=? and has = 1)";
        return jdbc.query(sql, new TPermissionMapper(), departmentId);
    }

    @Override
    public boolean addRootMenu(String name) {
        String sql = "insert into t_permission(name) values(?)";
        return jdbc.update(sql, name) > 0;
    }

    @Override
    public boolean updatePermission(String id, String field, String value) {
        if (field.equals("parentId")) {
            field = "parent_id";
        }
        if (field.equals("isLink")) {
            field = "is_link";
        }
        String sql = "update t_permission set " + field + "=? where id=?";
        if (jdbc.update(sql, value, id) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePermission(String id) {
        String sql = "select count(0) from t_permission where parent_id=?";
        if (jdbc.queryForObject(sql, Integer.class, id) == 0) {
            sql = "delete from t_permission where id=?";
            if (jdbc.update(sql, id) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addChildMenu(String parentId, String name) {
        String sql = "insert into t_permission(parent_id,name) values(?,?)";
        if (jdbc.update(sql, parentId, name) > 0) {
            return true;
        }
        return false;
    }
}
