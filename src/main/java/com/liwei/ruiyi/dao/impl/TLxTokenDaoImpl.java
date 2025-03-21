package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TLxToken;
import com.liwei.ruiyi.bo.mapper.TLxTokenMapper;
import com.liwei.ruiyi.dao.TLxTokenDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@Repository("lxTokenDao")
public class TLxTokenDaoImpl implements TLxTokenDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public int insertToken(TLxToken token) {
        String sql = "insert into t_lx_token (access_token,refresh_token,save_time,expires_time) values(?,?,?,?)";
        Object[] args = {token.getAccessToken(), token.getRefreshToken(), token.getSaveTime(), token.getExpiresTime()};
        return jdbc.update(sql, args);
    }

    @Override
    public int updateToken(TLxToken token) {
        return 0;
    }

    @Override
    public TLxToken getToken() {
        String sql = "select * from t_lx_token";
        List<TLxToken> list = jdbc.query(sql,new TLxTokenMapper());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int getTokenCount() {
        String sql = "select count(0) from t_lx_token";
        return jdbc.queryForObject(sql, Integer.class);
    }
}
