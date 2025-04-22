package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.mapper.CDeclarationMapper;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.bo.mapper.TSellerMapper;
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
        String isAdmin = searchParams.getString("is_admin");
        String isLadder = searchParams.getString("is_ladder");
        String departmentCode = searchParams.getString("departmentCode");

        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from c_declaration where user_id = ?";
        String querySql = "select * from c_declaration where user_id = ? ";
        List<Object> args = new ArrayList<>();

        if (StringUtils.isNotBlank(isAdmin) && isAdmin.equals("1")) {
            sql = "select count(0) from c_declaration  where 1 = ? ";
            querySql = "select * from c_declaration where 1 = ? ";
            args.add(1);
        } else if (StringUtils.isNotBlank(isLadder) && isLadder.equals("1")) {
            sql = "select count(0) from c_declaration where user_id in (select id from t_user where department_code like ?)";
            querySql = "select * from c_declaration where user_id in (select id from t_user where department_code like ?)";
            args.add(departmentCode + "%");
        } else {
            args.add(user_id);
        }


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
    public boolean delDdeclaration(String id) {
        String sql = "delete from c_declaration where id = ?";
        int count = jdbc.update(sql, id);
        return count > 0;
    }

    @Override
    public boolean createDeclaration(CDeclaration dec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dec.getSellerId());

        sql = "insert into c_declaration(user_id,user_name,user_phone,pro_name,asin," +
                "image_path,purchase_packages,per_package_quantity,total_quantity,other," +
                "status,seller_id,seller_name,shc,link) values(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?)";
        Object[] args = {dec.getUserId(), dec.getUserName(), dec.getUserPhone(), dec.getProName(), dec.getAsin(),
                dec.getImagePath(), dec.getPurchasePackages(), dec.getPerPackageQuantity(), dec.getTotalQuantity(), dec.getOther(),
                "已申报", dec.getSellerId(), seller.getName(), dec.getShc(),dec.getLink()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public boolean updateDeclaration(CDeclaration dec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dec.getSellerId());

        sql = "update c_declaration set pro_name = ?,link = ?,asin = ?,seller_id = ?,seller_name = ?," +
                "shc = ?,image_path = ?,purchase_packages = ?,per_package_quantity = ?,total_quantity = ?," +
                "other = ? where id = ?";
        Object[] args = {dec.getProName(), dec.getLink(), dec.getAsin(), dec.getSellerId(), seller.getName(),
                dec.getShc(), dec.getImagePath(), dec.getPurchasePackages(), dec.getPerPackageQuantity(), dec.getTotalQuantity(),
                dec.getOther(), dec.getId()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public CDeclaration queryDeclarationById(String id) {
        String sql = "select * from c_declaration where id = ?";
        CDeclaration declaration = jdbc.queryForObject(sql, new CDeclarationMapper(), id);
        if (declaration != null) {
            return declaration;
        }
        return null;
    }

    @Override
    public boolean queren(String id) {
        String sql = "update c_declaration set status = '已确认',confirm_time = current_timestamp where id = ?";
        return jdbc.update(sql, id) > 0;
    }

    @Override
    public boolean updatePurcacheMsg(CDeclaration dbDec) {
        String sql = "select * from t_seller where sid = ?";
        TSeller seller = jdbc.queryForObject(sql, new TSellerMapper(), dbDec.getSellerId());

        sql = "update c_declaration set cost_price = ?,cost_all_price = ?,total_price = ?,buy_quantity = ?,plan_total_quantity = ?," +
                "purchase_time = current_timestamp,plan_ship_time = ?,seller_id = ?,seller_name = ?,status = '已采购' where id = ?";
        Object[] args = {dbDec.getCostPrice(), dbDec.getCostAllPrice(), dbDec.getTotalPrice(), dbDec.getBuyQuantity(), dbDec.getPlanTotalQuantity(),
                dbDec.getPlanShipTime(), dbDec.getSellerId(), seller.getName(), dbDec.getId()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public void uploadDeclarationFaPiao(String id, String src) {
        String sql = "update c_declaration set fapiao = ?,fapiao_time = current_timestamp where id = ?";
        jdbc.update(sql, src, id);
    }

    @Override
    public void uploadDeclarationTips(String id, String src) {
        String sql = "update c_declaration set tips = ?,tips_time = current_timestamp where id = ?";
        jdbc.update(sql, src, id);
    }

    @Override
    public boolean arrival(String id) {
        String sql = "update c_declaration set status = '已到货',ship_time = current_timestamp where id = ?";
        return jdbc.update(sql, id) > 0;
    }
}
