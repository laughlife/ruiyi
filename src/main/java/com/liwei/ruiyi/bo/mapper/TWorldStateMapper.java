package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TWorldState;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TWorldStateMapper implements RowMapper<TWorldState> {
    @Override
    public TWorldState mapRow(ResultSet rs, int rowNum) throws SQLException {
        TWorldState obj = new TWorldState();
        obj.setId(rs.getInt("id"));
        obj.setMid(rs.getInt("mid"));
        obj.setCountryCode(rs.getString("country_code"));
        obj.setStateOrProvinceName(rs.getString("state_or_province_name"));
        obj.setCode(rs.getString("code"));
        return obj;
    }
}
