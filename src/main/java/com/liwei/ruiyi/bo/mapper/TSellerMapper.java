package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TSeller;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TSellerMapper implements RowMapper<TSeller> {
    @Override
    public TSeller mapRow(ResultSet rs, int rowNum) throws SQLException {
        TSeller obj = new TSeller();
        obj.setSid(rs.getInt("sid"));
        obj.setMid(rs.getInt("mid"));
        obj.setName(rs.getString("name"));
        obj.setSellerId(rs.getString("seller_id"));
        obj.setAccountName(rs.getString("account_name"));
        obj.setSellerAccountId(rs.getInt("seller_account_id"));
        obj.setRegion(rs.getString("region"));
        obj.setCountry(rs.getString("country"));
        obj.setHasAdsSetting(rs.getInt("has_ads_setting"));
        obj.setMarketplaceId(rs.getString("marketplace_id"));
        obj.setStatus(rs.getInt("status"));
        return obj;
    }
}
