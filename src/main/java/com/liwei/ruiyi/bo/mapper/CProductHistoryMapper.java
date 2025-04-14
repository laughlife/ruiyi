package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CProductHistory;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CProductHistoryMapper implements RowMapper<CProductHistory> {
    @Override
    public CProductHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        CProductHistory obj = new CProductHistory();
        obj.setId(rs.getInt("id"));
        obj.setProId(rs.getInt("pro_id"));
        obj.setName(rs.getString("name"));
        obj.setLink(rs.getString("link"));
        obj.setImageUrl(rs.getString("image_url"));
        obj.setSupplierId(rs.getInt("supplier_id"));
        obj.setSupplierName(rs.getString("supplier_name"));
        obj.setCostPrice(rs.getBigDecimal("cost_price"));
        obj.setIsActive(rs.getBoolean("is_active"));
        obj.setCreateTime(rs.getString("create_time"));
        obj.setUpdateTime(rs.getString("update_time"));
        obj.setDisableTime(rs.getString("disable_time"));
        obj.setUnshipQuantity(rs.getInt("unship_quantity"));
        obj.setUnshipPrice(rs.getBigDecimal("unship_price"));
        obj.setOther(rs.getString("other"));
        obj.setHistory(rs.getString("history"));
        return obj;
    }
}
