package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.dao.CDeclarationDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("declarationDao")
public class CDeclarationDaoImpl implements CDeclarationDao {
    @Autowired
    private JdbcTemplate jdbc;
}
