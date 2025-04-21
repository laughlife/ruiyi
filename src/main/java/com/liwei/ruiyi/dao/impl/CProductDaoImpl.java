package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.mapper.CProductMapper;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.dao.CProductDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository("cproductDao")
public class CProductDaoImpl implements CProductDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public PageUtils queryProductByPage(PageUtils page) {
        JSONObject searchParams = page.getSearchParams();
        String key = searchParams.getString("key");
        String supplier_id = searchParams.getString("supplier_id");
        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from c_product where is_active = ?";
        String querySql = "select * from c_product where is_active = ? ";
        List<Object> args = new ArrayList<>();
        args.add(1);
        if (StringUtils.isNotBlank(key)) {
            key = "%" + key.trim() + "%";
            sql += " and name like ?";
            querySql += " and name like ?";
            args.add(key);
        }

        if (StringUtils.isNotBlank(supplier_id)) {
            sql += " and supplier_id = ?";
            querySql += " and supplier_id = ?";
            args.add(supplier_id);
        }

        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);

        querySql += " limit ?,?";
        args.add(pageStart);
        args.add(limit);

        List<CProduct> supplierList = jdbc.query(querySql, new CProductMapper(), args.toArray());
        page.setData(supplierList);

        return page;
    }


    @Override
    public boolean createProduct(CProduct product) {
        int supplierId = product.getSupplierId();
        CSupplier supplier = jdbc.queryForObject("select * from c_supplier where id = ?", new CSupplierMapper(), supplierId);
        if (supplier == null) {
            return false;
        }
        String sql = "insert into c_product(name,link,image_url,supplier_id,supplier_name," +
                "cost_price,other) values(?,?,?,?,?," +
                "?,?)";
        Object[] args = {product.getName(), product.getLink(), product.getImageUrl(), product.getSupplierId(), supplier.getName(),
                product.getCostPrice(), product.getOther()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public CProduct queryProductById(String id) {
        CProduct product = jdbc.queryForObject("select * from c_product where id = ?", new CProductMapper(), id);
        return product;
    }

    @Override
    public int updateProduct(CProduct product) {
        //1. 首先备份现有状态到备份表中
        String sql = "update c_product set is_active = 0,disable_time = current_timestamp where id = ?";
        jdbc.update(sql, product.getId());
        String insertSql = "INSERT INTO c_product (name, link, image_url, supplier_id, supplier_name, " +
                "cost_price, is_active, create_time, update_time, disable_time, " +
                "unship_quantity, unship_price, other, history)" +
                "SELECT name, link, image_url, supplier_id, supplier_name, " +
                "cost_price, 1, create_time, update_time, null, " +
                "unship_quantity, unship_price, other, history " +
                "FROM c_product " +
                "WHERE id = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getId());
            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        //2. 开始更新商品信息
        sql = "select name from c_supplier where id = ?";
        String supplierName = jdbc.queryForObject(sql, String.class, product.getSupplierId());

        sql = "update c_product set name = ?,link = ?,image_url = ?,supplier_id = ?,supplier_name = ?," +
                "cost_price = ?,other = ?,update_time = current_timestamp where id = ?";
        Object[] args = {product.getName(), product.getLink(), product.getImageUrl(), product.getSupplierId(), supplierName,
                product.getCostPrice(), product.getOther(), id};
        jdbc.update(sql, args);

        return id;
    }

    @Override
    public JSONObject deleteProduct(String id) {
        JSONObject json = new JSONObject();
        if(StringUtils.isNotBlank(id)){
            String sql = "update c_product set is_active = 0,disable_time = current_timestamp where id = ?";
            int count = jdbc.update(sql, id);
            if(count > 0){
                json.put("status", true);
                json.put("msg", "删除成功。");
            }else{
                json.put("status", false);
                json.put("msg", "删除失败，请刷新页面后重试。");
            }
        }else{
            json.put("status", false);
            json.put("msg", "删除失败，请刷新页面后重试。");
        }
        return json;
    }
}
