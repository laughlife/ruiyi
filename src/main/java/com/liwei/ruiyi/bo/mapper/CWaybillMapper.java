package com.liwei.ruiyi.bo.mapper;

import com.liwei.ruiyi.bo.CWaybill;
import org.springframework.jdbc.core.RowMapper;
import java.sql.*;

public class CWaybillMapper implements RowMapper<CWaybill> {
    @Override
    public CWaybill mapRow(ResultSet rs, int rowNum) throws SQLException {
        CWaybill obj = new CWaybill();
        obj.setId(rs.getInt("id"));
        obj.setWaybillNumber(rs.getString("waybill_number"));
        obj.setDeclarationId(rs.getInt("declaration_id"));
        obj.setWaybillImage(rs.getString("waybill_image"));
        obj.setPackageCount(rs.getInt("package_count"));
        obj.setPerPackageQuantity(rs.getInt("per_package_quantity"));
        obj.setTotalQuantity(rs.getInt("total_quantity"));
        obj.setTotalWeight(rs.getBigDecimal("total_weight"));
        obj.setFreight(rs.getBigDecimal("freight"));
        obj.setStatus(rs.getString("status"));
        obj.setShipTime(rs.getString("ship_time"));
        obj.setReceiveTime(rs.getString("receive_time"));
        obj.setPlanReceiveTime(rs.getString("plan_receive_time"));
        return obj;
    }
}
