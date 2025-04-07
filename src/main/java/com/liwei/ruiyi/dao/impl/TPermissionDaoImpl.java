package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.TPermissionDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("permissionDao")
public class TPermissionDaoImpl implements TPermissionDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }
}
