package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TAdmin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TAdminRowMapper implements RowMapper<TAdmin> {
    @Override
    public TAdmin mapRow(ResultSet rs, int rowNum) throws SQLException {
        TAdmin t_admin = new TAdmin();
        t_admin.setId(rs.getInt("id"));
        t_admin.setUsername(rs.getString("username"));
        t_admin.setPassword(rs.getString("password"));
        t_admin.setName(rs.getString("name"));
        t_admin.setPhone(rs.getString("phone"));
        return t_admin;
    }
}