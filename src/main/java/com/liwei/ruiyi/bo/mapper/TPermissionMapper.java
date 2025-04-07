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
        obj.setParentId(rs.getInt("parent_id"));
        obj.setDataScope(rs.getString("data_scope"));
        obj.setIcon(rs.getString("icon"));
        obj.setPath(rs.getString("path"));
        obj.setDescription(rs.getString("description"));
        obj.setPx(rs.getInt("px"));
        obj.setIsLink(rs.getInt("is_link"));
        return obj;
    }
}
