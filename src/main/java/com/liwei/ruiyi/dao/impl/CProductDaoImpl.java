package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.CProductDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("cproductDao")
public class CProductDaoImpl implements CProductDao {
    @Autowired
    private JdbcTemplate jdbc;
}
