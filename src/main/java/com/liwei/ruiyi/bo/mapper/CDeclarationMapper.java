package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CDeclaration;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CDeclarationMapper implements RowMapper<CDeclaration> {
    @Override
    public CDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {
        CDeclaration obj = new CDeclaration();
        obj.setId(rs.getInt("id"));
        obj.setUserId(rs.getInt("user_id"));
        obj.setUserName(rs.getString("user_name"));
        obj.setUserPhone(rs.getString("user_phone"));
        obj.setSellerId(rs.getInt("seller_id"));
        obj.setSellerName(rs.getString("seller_name"));
        obj.setProId(rs.getInt("pro_id"));
        obj.setProName(rs.getString("pro_name"));
        obj.setTips(rs.getString("tips"));
        obj.setFapiao(rs.getString("fapiao"));
        obj.setShc(rs.getString("shc"));
        obj.setLink(rs.getString("link"));
        obj.setAsin(rs.getString("asin"));
        obj.setImagePath(rs.getString("image_path"));
        obj.setPurchasePackages(rs.getInt("purchase_packages"));
        obj.setPerPackageQuantity(rs.getInt("per_package_quantity"));
        obj.setTotalQuantity(rs.getInt("total_quantity"));
        obj.setKcsl(rs.getInt("kcsl"));
        obj.setKcyl(rs.getInt("kcyl"));
        obj.setKcdj(rs.getBigDecimal("kcdj"));
        obj.setKsjz(rs.getBigDecimal("ksjz"));
        obj.setCostPrice(rs.getBigDecimal("cost_price"));
        obj.setBuyQuantity(rs.getInt("buy_quantity"));
        obj.setCostAllPrice(rs.getBigDecimal("cost_all_price"));
        obj.setTotalPrice(rs.getBigDecimal("total_price"));
        obj.setPlanTotalQuantity(rs.getInt("plan_total_quantity"));
        obj.setOther(rs.getString("other"));
        obj.setShippedQuantity(rs.getInt("shipped_quantity"));
        obj.setReceivedQuantity(rs.getInt("received_quantity"));
        obj.setDeclareTime(rs.getString("declare_time"));
        obj.setConfirmTime(rs.getString("confirm_time"));
        obj.setPurchaseTime(rs.getString("purchase_time"));
        obj.setPlanShipTime(rs.getString("plan_ship_time"));
        obj.setShipTime(rs.getString("ship_time"));
        obj.setFapiaoTime(rs.getString("fapiao_time"));
        obj.setTipsTime(rs.getString("tips_time"));
        obj.setSendTime(rs.getString("send_time"));
        obj.setSendQuantity(rs.getInt("send_quantity"));
        obj.setPlanReceiveTime(rs.getString("plan_receive_time"));
        obj.setStatus(rs.getString("status"));
        return obj;
    }
}
