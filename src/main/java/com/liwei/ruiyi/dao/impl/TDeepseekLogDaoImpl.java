package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TDeepseekLog;
import com.liwei.ruiyi.dao.TDeepseekLogDao;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository("deepseeklogDao")
public class TDeepseekLogDaoImpl implements TDeepseekLogDao {

    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }


    @Override
    public int insert(TDeepseekLog entity) {
        String sql = "insert into t_deepseek_log(question, answer, qt, at) values(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getQuestion());
            ps.setString(2, entity.getAnswer());
            ps.setString(3, entity.getQt());
            ps.setString(4, entity.getAt());
            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKey().intValue();
        entity.setId(id);
        return id > 0 ? id : 0;
    }

    @Override
    public int update(TDeepseekLog entity) {
        String sql = "update t_deepseek_log set answer = ?, at = ? where id = ?";
        Object[] args = new Object[]{entity.getAnswer(), entity.getAt(), entity.getId()};
        return jdbc.update(sql, args);
    }

    @Override
    public int deleteById(Integer id) {
        // todo 未实现，也未删除
        return 0;
    }

    @Override
    public TDeepseekLog selectById(Integer id) {
        // todo 未实现，也未删除
        return null;
    }

    @Override
    public List<TDeepseekLog> selectAll() {
        // todo 未实现，也未删除
        return null;
    }
}
