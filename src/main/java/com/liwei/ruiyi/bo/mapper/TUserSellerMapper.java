package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TUserSeller;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TUserSellerMapper implements RowMapper<TUserSeller> {
    @Override
    public TUserSeller mapRow(ResultSet rs, int rowNum) throws SQLException {
        TUserSeller obj = new TUserSeller();
        obj.setId(rs.getInt("id"));
        obj.setUserId(rs.getInt("user_id"));
        obj.setSellerId(rs.getInt("seller_id"));
        return obj;
    }
}
