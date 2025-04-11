package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.mapper.CSupplierMapper;
import com.liwei.ruiyi.dao.CSupplierDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository("supplierDao")
public class CSupplierDaoImpl implements CSupplierDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public PageUtils querySupplierByPage(PageUtils page) {
        JSONObject searchParams = page.getSearchParams();
        String key = searchParams.getString("key");
        int pageStart = page.getPageStart();
        int limit = page.getLimit();

        String sql = "select count(0) from c_supplier where 1 = ?";
        String querySql = "select * from c_supplier where 1 = ? ";
        List<Object> args = new ArrayList<>();
        args.add(1);
        if (StringUtils.isNotBlank(key)) {
            key = "%" + key.trim() + "%";
            sql += " and  name like ?";
            querySql += " and  name like ?";
            args.add(key);
        }

        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);

        querySql += " limit ?,?";
        args.add(pageStart);
        args.add(limit);

        List<CSupplier> supplierList = jdbc.query(querySql, new CSupplierMapper(), args.toArray());
        page.setData(supplierList);

        return page;
    }

    @Override
    public boolean createSupplier(CSupplier supplier) {
        String sql = "insert into c_supplier(name,fzr,phone,address,other_info) values(?,?,?,?,?)";
        Object[] args = {supplier.getName(), supplier.getFzr(), supplier.getPhone(), supplier.getAddress(), supplier.getOtherInfo()};
        int count = jdbc.update(sql, args);
        return count > 0;
    }

    @Override
    public boolean updateSupplier(String id, String field, String value) {
        if(StringUtils.isNotBlank(field) && field.equals("otherInfo")){
            field = "other_info";
        }
        String sql = "update c_supplier set " + field + " = ? where id = ?";
        int count = jdbc.update(sql, value, id);
        return count > 0;
    }
}
