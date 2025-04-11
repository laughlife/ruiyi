package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.CWaybillDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("waybillDao")
public class CWaybillDaoImpl implements CWaybillDao {
    @Autowired
    private JdbcTemplate jdbc;
}
