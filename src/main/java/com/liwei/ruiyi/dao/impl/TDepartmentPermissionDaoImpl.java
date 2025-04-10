package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TDepartmentPermission;
import com.liwei.ruiyi.dao.TDepartmentPermissionDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository("departmentPermissionDao")
public class TDepartmentPermissionDaoImpl implements TDepartmentPermissionDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<TDepartmentPermission> getPermissionByDepartmentId(String departmentId) {
        return List.of();
    }
}
