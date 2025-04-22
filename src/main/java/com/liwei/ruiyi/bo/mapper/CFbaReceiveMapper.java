package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CFbaReceive;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CFbaReceiveMapper implements RowMapper<CFbaReceive> {
    @Override
    public CFbaReceive mapRow(ResultSet rs, int rowNum) throws SQLException {
        CFbaReceive obj = new CFbaReceive();
        obj.setId(rs.getInt("id"));
        obj.setDecId(rs.getInt("dec_id"));
        obj.setReceiveQuantity(rs.getInt("receive_quantity"));
        obj.setReceiveTime(rs.getString("receive_time"));
        return obj;
    }
}
