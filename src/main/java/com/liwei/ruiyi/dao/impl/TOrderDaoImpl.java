package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.TOrderDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("orderDao")
public class TOrderDaoImpl implements TOrderDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }
}
