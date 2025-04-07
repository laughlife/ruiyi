package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TDepartment;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TDepartmentMapper implements RowMapper<TDepartment> {
    @Override
    public TDepartment mapRow(ResultSet rs, int rowNum) throws SQLException {
        TDepartment obj = new TDepartment();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setCode(rs.getString("code"));
        obj.setParentId(rs.getInt("parent_id"));
        obj.setLevel(rs.getInt("level"));
        obj.setPath(rs.getString("path"));
        return obj;
    }
}
