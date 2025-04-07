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
}
