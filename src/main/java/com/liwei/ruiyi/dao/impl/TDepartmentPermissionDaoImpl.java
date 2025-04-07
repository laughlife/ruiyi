package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.TDepartmentPermissionDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("departmentpermissionDao")
public class TDepartmentPermissionDaoImpl implements TDepartmentPermissionDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }
}
