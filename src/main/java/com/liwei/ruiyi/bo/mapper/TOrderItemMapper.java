package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.TOrderItem;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class TOrderItemMapper implements RowMapper<TOrderItem> {
    @Override
    public TOrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        TOrderItem obj = new TOrderItem();
        obj.setId(rs.getLong("id"));
        obj.setOrderId(rs.getString("order_id"));
        obj.setAsin(rs.getString("asin"));
        obj.setQuantityOrdered(rs.getInt("quantity_ordered"));
        obj.setSellerSku(rs.getString("seller_sku"));
        obj.setLocalSku(rs.getString("local_sku"));
        obj.setLocalName(rs.getString("local_name"));
        obj.setOrderStatus(rs.getString("order_status"));
        obj.setProfit(rs.getBigDecimal("profit"));
        obj.setWhType(rs.getString("whType"));
        obj.setTitle(rs.getString("title"));
        obj.setAsinUrl(rs.getString("asin_url"));
        obj.setSid(rs.getInt("sid"));
        obj.setSku(rs.getString("sku"));
        obj.setProductId(rs.getInt("product_id"));
        obj.setProductName(rs.getString("product_name"));
        obj.setPicUrl(rs.getString("pic_url"));
        obj.setOrderItemId(rs.getLong("order_item_id"));
        obj.setPointsMonetaryValueAmount(rs.getBigDecimal("points_monetary_value_amount"));
        obj.setQuantityShipped(rs.getInt("quantity_shipped"));
        obj.setItemPriceAmount(rs.getBigDecimal("item_price_amount"));
        obj.setItemPriceAmountEstimated(rs.getBigDecimal("item_price_amount_estimated"));
        obj.setItemTaxAmount(rs.getBigDecimal("item_tax_amount"));
        obj.setShippingPriceAmount(rs.getBigDecimal("shipping_price_amount"));
        obj.setShippingTaxAmount(rs.getBigDecimal("shipping_tax_amount"));
        obj.setGiftWrapPriceAmount(rs.getBigDecimal("gift_wrap_price_amount"));
        obj.setGiftWrapTaxAmount(rs.getBigDecimal("gift_wrap_tax_amount"));
        obj.setShippingDiscountAmount(rs.getBigDecimal("shipping_discount_amount"));
        obj.setCodFeeAmount(rs.getBigDecimal("cod_fee_amount"));
        obj.setPromotionIds(rs.getString("promotion_ids"));
        obj.setShippingDiscountTaxAmount(rs.getBigDecimal("shipping_discount_tax_amount"));
        obj.setPromotionDiscountAmount(rs.getBigDecimal("promotion_discount_amount"));
        obj.setPromotionDiscountTaxAmount(rs.getBigDecimal("promotion_discount_tax_amount"));
        obj.setCodFeeDiscountAmount(rs.getBigDecimal("cod_fee_discount_amount"));
        obj.setGiftMessageText(rs.getString("gift_message_text"));
        obj.setBuyerCustomizedInfo(rs.getString("buyer_customized_info"));
        obj.setIsBuyerRequestedCancel(rs.getString("is_buyer_requested_cancel"));
        obj.setBuyerCancelReason(rs.getString("buyer_cancel_reason"));
        obj.setGiftWrapLevel(rs.getString("gift_wrap_level"));
        obj.setConditionNote(rs.getString("condition_note"));
        obj.setConditionId(rs.getString("condition_id"));
        obj.setConditionSubtypeId(rs.getString("condition_subtype_id"));
        obj.setScheduledDeliveryStartDate(rs.getString("scheduled_delivery_start_date"));
        obj.setScheduledDeliveryEndDate(rs.getString("scheduled_delivery_end_date"));
        obj.setPriceDesignation(rs.getBigDecimal("price_designation"));
        obj.setCgPrice(rs.getBigDecimal("cg_price"));
        obj.setFeeName(rs.getString("fee_name"));
        obj.setCgTransportCosts(rs.getBigDecimal("cg_transport_costs"));
        obj.setFbaShipmentAmount(rs.getBigDecimal("fba_shipment_amount"));
        obj.setFbaShipmentAmountEstimated(rs.getBigDecimal("fba_shipment_amount_estimated"));
        obj.setCommissionAmount(rs.getBigDecimal("commission_amount"));
        obj.setCommissionAmountEstimated(rs.getBigDecimal("commission_amount_estimated"));
        obj.setOtherAmount(rs.getBigDecimal("other_amount"));
        obj.setFeeCurrency(rs.getString("fee_currency"));
        obj.setFeeIcon(rs.getString("fee_icon"));
        obj.setFeeCostAmount(rs.getBigDecimal("fee_cost_amount"));
        obj.setFeeCost(rs.getBigDecimal("fee_cost"));
        obj.setSalesPriceAmount(rs.getBigDecimal("sales_price_amount"));
        obj.setUnitPriceAmount(rs.getBigDecimal("unit_price_amount"));
        obj.setTaxAmount(rs.getBigDecimal("tax_amount"));
        obj.setItemTaxAmountEstimated(rs.getBigDecimal("item_tax_amount_estimated"));
        obj.setPromotionAmount(rs.getBigDecimal("promotion_amount"));
        obj.setItemDiscount(rs.getBigDecimal("item_discount"));
        return obj;
    }
}
