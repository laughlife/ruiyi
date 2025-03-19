package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TFont;
import com.liwei.ruiyi.bo.mapper.TFontRowMapper;
import com.liwei.ruiyi.dao.TFontDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("fontDao")
public class TFontDaoImpl implements TFontDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public List<TFont> getAllFont() {
        return jdbc.query("select * from t_font", new TFontRowMapper());
    }
}