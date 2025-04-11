package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CSupplier;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CSupplierMapper implements RowMapper<CSupplier> {
    @Override
    public CSupplier mapRow(ResultSet rs, int rowNum) throws SQLException {
        CSupplier obj = new CSupplier();
        obj.setId(rs.getInt("id"));
        obj.setName(rs.getString("name"));
        obj.setPhone(rs.getString("phone"));
        obj.setAddress(rs.getString("address"));
        obj.setOtherInfo(rs.getString("other_info"));
        return obj;
    }
}
