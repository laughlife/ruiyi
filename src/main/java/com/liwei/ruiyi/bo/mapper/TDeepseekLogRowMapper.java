package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TDeepseekLog;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TDeepseekLogRowMapper implements RowMapper<TDeepseekLog> {
    @Override
    public TDeepseekLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        TDeepseekLog obj = new TDeepseekLog();
        obj.setId(rs.getInt("id"));
        obj.setQuestion(rs.getString("question"));
        obj.setQt(rs.getString("qt"));
        obj.setAnswer(rs.getString("answer"));
        obj.setAt(rs.getString("at"));
        return obj;
    }
}