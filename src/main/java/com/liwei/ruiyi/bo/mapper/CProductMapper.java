package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CProduct;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CProductMapper implements RowMapper<CProduct> {
    @Override
    public CProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        CProduct obj = new CProduct();
        obj.setId(rs.getInt("id"));
        obj.setProductName(rs.getString("product_name"));
        obj.setImageUrl(rs.getString("image_url"));
        obj.setSupplierId(rs.getInt("supplier_id"));
        obj.setSupplierName(rs.getString("supplier_name"));
        obj.setCostPrice(rs.getBigDecimal("cost_price"));
        obj.setIsActive(rs.getBoolean("is_active"));
        obj.setCreateTime(rs.getString("create_time"));
        obj.setUpdateTime(rs.getString("update_time"));
        obj.setDisableTime(rs.getString("disable_time"));
        obj.setUnshipQuantity(rs.getInt("unship_quantity"));
        return obj;
    }
}
