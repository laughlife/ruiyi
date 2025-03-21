package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TLxToken;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TLxTokenMapper implements RowMapper<TLxToken> {
    @Override
    public TLxToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        TLxToken obj = new TLxToken();
        obj.setId(rs.getInt("id"));
        obj.setAccessToken(rs.getString("access_token"));
        obj.setRefreshToken(rs.getString("refresh_token"));
        obj.setSaveTime(rs.getLong("save_time"));
        obj.setExpiresTime(rs.getLong("expires_time"));
        return obj;
    }
}
