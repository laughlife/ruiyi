package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.mapper.CDeclarationMapper;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.dao.CDeclarationDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository("declarationDao")
public class CDeclarationDaoImpl implements CDeclarationDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public PageUtils queryMyDeclaration(PageUtils page) {
        JSONObject searchParams = page.getSearchParams();
        String key = searchParams.getString("key");
        String user_id = searchParams.getString("user_id");
        String date_start = searchParams.getString("date_start");
        String date_end = searchParams.getString("date_end");

        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from c_declaration where user_id = ?";
        String querySql = "select * from c_declaration where user_id = ? ";
        List<Object> args = new ArrayList<>();
        args.add(user_id);

        if (StringUtils.isNotBlank(key)) {
            key = "%" + key.trim() + "%";
            sql += " and pro_name like ?";
            querySql += " and pro_name like ?";
            args.add(key);
        }
        if (StringUtils.isNotBlank(date_start)) {
            date_start = date_start.trim() + " 00:00:00";
            sql += " and declare_time >= ?";
            querySql += " and declare_time >= ?";
            args.add(date_start);
        }
        if (StringUtils.isNotBlank(date_end)) {
            date_end = date_end.trim() + " 23:59:59";
            sql += " and declare_time <= ?";
            querySql += " and declare_time <= ?";
            args.add(date_end);
        }

        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);

        querySql += " limit ?,?";
        args.add(pageStart);
        args.add(limit);

        List<CDeclaration> supplierList = jdbc.query(querySql, new CDeclarationMapper(), args.toArray());
        page.setData(supplierList);

        return page;
    }

    @Override
    public boolean declarationDao(String id) {
        String sql = "delete from c_supplier where id = ?";
        int count = jdbc.update(sql, id);
        return count > 0;
    }

    @Override
    public boolean createDeclaration(CDeclaration declaration) {
        String sql = "insert into c_declaration(user_id,user_name,user_phone,pro_name,asin," +
                "image_path,purchase_packages,per_package_quantity,total_quantity,other," +
                "status) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?)";
        Object[] args = {declaration.getUserId(), declaration.getUserName(), declaration.getUserPhone(),declaration.getProName(),declaration.getAsin(),
                declaration.getImagePath(),declaration.getPurchasePackages(),declaration.getPerPackageQuantity(),declaration.getTotalQuantity(),declaration.getOther(),
                "已申报"};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public boolean updateDeclaration(String id, String field, String value) {
        switch (field){
            case "purchasePackages":
                field = "purchase_packages";
                break;
            case "perPackageQuantity":
                field = "per_package_quantity";
                break;
            case "totalQuantity":
                field = "total_quantity";
                break;
            case "shippedQuantity":
                field = "shipped_quantity";
                break;
            case "receivedQuantity":
                field = "received_quantity";
                break;
            case "imagePath":
                field = "image_path";
                break;
            case "declareTime":
                field = "declare_time";
                break;
            default:
                break;
        }

        String sql = "update c_declaration set " + field + " = ? where id = ?";
        int count = jdbc.update(sql, value, id);
        return count > 0;
    }
}
