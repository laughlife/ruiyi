package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.PersistentLogins;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class PersistentLoginsMapper implements RowMapper<PersistentLogins> {
    @Override
    public PersistentLogins mapRow(ResultSet rs, int rowNum) throws SQLException {
        PersistentLogins obj = new PersistentLogins();
        obj.setUsername(rs.getString("username"));
        obj.setSeries(rs.getString("series"));
        obj.setToken(rs.getString("token"));
        obj.setLastUsed(rs.getString("last_used"));
        obj.setDeviceInfo(rs.getString("device_info"));
        obj.setIpAddress(rs.getString("ip_address"));
        return obj;
    }
}
