package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TMarketplace;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TMarketplaceMapper implements RowMapper<TMarketplace> {
    @Override
    public TMarketplace mapRow(ResultSet rs, int rowNum) throws SQLException {
        TMarketplace obj = new TMarketplace();
        obj.setMid(rs.getInt("mid"));
        obj.setRegion(rs.getString("region"));
        obj.setAwsRegion(rs.getString("aws_region"));
        obj.setCountry(rs.getString("country"));
        obj.setCode(rs.getString("code"));
        obj.setMarketplaceId(rs.getString("marketplace_id"));
        return obj;
    }
}
