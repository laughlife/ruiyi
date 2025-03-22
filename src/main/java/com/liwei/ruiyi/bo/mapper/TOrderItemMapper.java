package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TOrderItem;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TOrderItemMapper implements RowMapper<TOrderItem> {
    @Override
    public TOrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        TOrderItem obj = new TOrderItem();
        obj.setId(rs.getString("id"));
        obj.setOrderId(rs.getString("order_id"));
        obj.setAsin(rs.getString("asin"));
        obj.setQuantityOrdered(rs.getInt("quantity_ordered"));
        obj.setSellerSku(rs.getString("seller_sku"));
        obj.setLocalSku(rs.getString("local_sku"));
        obj.setLocalName(rs.getString("local_name"));
        return obj;
    }
}
