package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TPermission;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TPermissionMapper implements RowMapper<TPermission> {
    @Override
    public TPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
        TPermission obj = new TPermission();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setDataScope(rs.getString("data_scope"));
        obj.setPath(rs.getString("path"));
        obj.setDescription(rs.getString("description"));
        return obj;
    }
}
