package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CDeclaration;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CDeclarationMapper implements RowMapper<CDeclaration> {
    @Override
    public CDeclaration mapRow(ResultSet rs, int rowNum) throws SQLException {
        CDeclaration obj = new CDeclaration();
        obj.setId(rs.getInt("id"));
        obj.setDeclarantId(rs.getInt("declarant_id"));
        obj.setDeclarantName(rs.getString("declarant_name"));
        obj.setDeclarantPhone(rs.getString("declarant_phone"));
        obj.setDeclareQuantity(rs.getInt("declare_quantity"));
        obj.setPurchasePackages(rs.getInt("purchase_packages"));
        obj.setPerPackageQuantity(rs.getInt("per_package_quantity"));
        obj.setTotalQuantity(rs.getInt("total_quantity"));
        obj.setShippedQuantity(rs.getInt("shipped_quantity"));
        obj.setReceivedQuantity(rs.getInt("received_quantity"));
        obj.setPurchaseTime(rs.getString("purchase_time"));
        obj.setPlanShipTime(rs.getString("plan_ship_time"));
        obj.setPlanReceiveTime(rs.getString("plan_receive_time"));
        obj.setStatus(rs.getString("status"));
        return obj;
    }
}
