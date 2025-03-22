package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TCurrencyInfo;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TCurrencyInfoMapper implements RowMapper<TCurrencyInfo> {
    @Override
    public TCurrencyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TCurrencyInfo obj = new TCurrencyInfo();
        obj.setCode(rs.getString("code"));
        obj.setName(rs.getString("name"));
        obj.setSymbol(rs.getString("symbol"));
        obj.setMinorUnits(rs.getBoolean("minor_units"));
        obj.setIsActive(rs.getBoolean("is_active"));
        return obj;
    }
}
