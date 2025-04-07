package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.TDepartmentDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("departmentDao")
public class TDepartmentDaoImpl implements TDepartmentDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }
}
