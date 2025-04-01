package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TProHistory;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TProHistoryMapper implements RowMapper<TProHistory> {
    @Override
    public TProHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        TProHistory obj = new TProHistory();
        obj.setMid(rs.getInt("mid"));
        obj.setCountry(rs.getString("country"));
        obj.setSid(rs.getInt("sid"));
        obj.setSellerName(rs.getString("seller_name"));
        obj.setAsin(rs.getString("asin"));
        obj.setSellerSku(rs.getString("seller_sku"));
        obj.setQueryDate(rs.getString("query_date"));
        obj.setIsEur(rs.getString("is_eur"));
        obj.setLocalSku(rs.getString("local_sku"));
        obj.setLocalName(rs.getString("local_name"));
        obj.setIsDelete(rs.getString("is_delete"));
        obj.setVolume(rs.getInt("volume"));
        obj.setProductPicUrl(rs.getString("product_pic_url"));
        obj.setSmallImageUrl(rs.getString("small_image_url"));
        obj.setPrice(rs.getBigDecimal("price"));
        obj.setSourceRate(rs.getBigDecimal("source_rate"));
        obj.setStatus(rs.getInt("status"));
        obj.setCid(rs.getInt("cid"));
        obj.setAddDate(rs.getString("add_date"));
        return obj;
    }
}
