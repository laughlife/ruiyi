package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TExchangeRate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TExchangeRateMapper implements RowMapper<TExchangeRate> {
    @Override
    public TExchangeRate mapRow(ResultSet rs, int rowNum) throws SQLException {
        TExchangeRate obj = new TExchangeRate();
        obj.setBaseCurrency(rs.getString("base_currency"));
        obj.setTargetCurrency(rs.getString("target_currency"));
        obj.setDate(rs.getString("date"));
        obj.setOfficialRate(rs.getBigDecimal("official_rate"));
        obj.setCustomRate(rs.getBigDecimal("custom_rate"));
        obj.setLastUpdated(rs.getString("last_updated"));
        return obj;
    }
}
