package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TOrder;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TOrderMapper implements RowMapper<TOrder> {
    @Override
    public TOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        TOrder obj = new TOrder();
        obj.setAmazonOrderId(rs.getString("amazon_order_id"));
        obj.setSid(rs.getInt("sid"));
        obj.setSellerName(rs.getString("seller_name"));
        obj.setOrderStatus(rs.getString("order_status"));
        obj.setOrderTotalAmount(rs.getBigDecimal("order_total_amount"));
        obj.setFulfillmentChannel(rs.getString("fulfillment_channel"));
        obj.setPostalCode(rs.getString("postal_code"));
        obj.setIsReturn(rs.getInt("is_return"));
        obj.setIsMcfOrder(rs.getInt("is_mcf_order"));
        obj.setIsAssessed(rs.getInt("is_assessed"));
        obj.setIsReplacedOrder(rs.getInt("is_replaced_order"));
        obj.setIsReplacementOrder(rs.getInt("is_replacement_order"));
        obj.setIsReturnOrder(rs.getInt("is_return_order"));
        obj.setOrderTotalCurrencyCode(rs.getString("order_total_currency_code"));
        obj.setSalesChannel(rs.getString("sales_channel"));
        obj.setTrackingNumber(rs.getString("tracking_number"));
        obj.setRefundAmount(rs.getBigDecimal("refund_amount"));
        obj.setPurchaseDateLocal(rs.getString("purchase_date_local"));
        obj.setPurchaseDateLocalUtc(rs.getString("purchase_date_local_utc"));
        obj.setShipmentDate(rs.getString("shipment_date"));
        obj.setShipmentDateUtc(rs.getString("shipment_date_utc"));
        obj.setShipmentDateLocal(rs.getString("shipment_date_local"));
        obj.setLastUpdateDate(rs.getString("last_update_date"));
        obj.setLastUpdateDateUtc(rs.getString("last_update_date_utc"));
        obj.setPostedDate(rs.getString("posted_date"));
        obj.setPostedDateUtc(rs.getString("posted_date_utc"));
        obj.setPurchaseDate(rs.getString("purchase_date"));
        obj.setPurchaseDateUtc(rs.getString("purchase_date_utc"));
        obj.setEarliestShipDate(rs.getString("earliest_ship_date"));
        obj.setEarliestShipDateUtc(rs.getString("earliest_ship_date_utc"));
        obj.setGmtModified(rs.getString("gmt_modified"));
        obj.setGmtModifiedUtc(rs.getString("gmt_modified_utc"));
        obj.setPhone(rs.getString("phone"));
        obj.setName(rs.getString("name"));
        obj.setAddress(rs.getString("address"));
        obj.setBuyerEmail(rs.getString("buyer_email"));
        obj.setBuyerName(rs.getString("buyer_name"));
        obj.setHideTime(rs.getString("hide_time"));
        return obj;
    }
}
