package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.CSupplierDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("supplierDao")
public class CSupplierDaoImpl implements CSupplierDao {
    @Autowired
    private JdbcTemplate jdbc;
}
