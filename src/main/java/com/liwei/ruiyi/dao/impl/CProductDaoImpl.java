package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.mapper.CProductMapper;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.dao.CProductDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
