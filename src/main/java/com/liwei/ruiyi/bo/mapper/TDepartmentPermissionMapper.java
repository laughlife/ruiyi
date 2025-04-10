package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TDepartmentPermission;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TDepartmentPermissionMapper implements RowMapper<TDepartmentPermission> {
    @Override
    public TDepartmentPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
        TDepartmentPermission obj = new TDepartmentPermission();
        obj.setDepartmentId(rs.getInt("department_id"));
        obj.setPermissionId(rs.getInt("permission_id"));
        obj.setHas(rs.getInt("has"));
        return obj;
    }
}
