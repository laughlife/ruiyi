package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TUser;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TUserMapper implements RowMapper<TUser> {
    @Override
    public TUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        TUser obj = new TUser();
        obj.setId(rs.getInt("id"));
        obj.setUsername(rs.getString("username"));
        obj.setPassword(rs.getString("password"));
        obj.setName(rs.getString("name"));
        obj.setPhone(rs.getString("phone"));
        obj.setDepartmentId(rs.getInt("department_id"));
        obj.setDepartmentCode(rs.getString("department_code"));
        obj.setIsLadder(rs.getInt("is_ladder"));
        obj.setIsAdmin(rs.getInt("is_admin"));
        obj.setCreateTime(rs.getString("create_time"));
        obj.setDeleteTime(rs.getString("delete_time"));
        obj.setIsBan(rs.getInt("is_ban"));
        return obj;
    }
}
