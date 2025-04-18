package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.mapper.TSellerMapper;
import com.liwei.ruiyi.dao.TSellerDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository("sellerDao")
public class TSellerDaoImpl implements TSellerDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdate(TSeller seller) {
        String sql = "select count(0) from t_seller where sid = ?";
        int count = jdbc.queryForObject(sql, new Object[]{seller.getSellerId()}, Integer.class);
        if (count > 0) {
            sql = "update t_seller set mid = ?, name = ?, seller_id = ?,account_name = ?, seller_account_id = ?, " +
                    "region = ?, country = ?, has_ads_setting = ?, marketplace_id = ?, status = ? " +
                    "where sid = ?";
            Object[] args = new Object[]{seller.getMid(), seller.getName(), seller.getSellerId(), seller.getAccountName(), seller.getSellerAccountId(),
                    seller.getRegion(), seller.getCountry(), seller.getHasAdsSetting(), seller.getMarketplaceId(), seller.getStatus(),
                    seller.getSid()};
            jdbc.update(sql, args);
        } else {
            sql = "insert into t_seller(sid, mid, name, seller_id, account_name," +
                    "seller_account_id, region, country, has_ads_setting, marketplace_id, " +
                    "`status`) values(?, ?, ?, ?, ?," +
                    " ?, ?, ?, ?, ?," +
                    " ?)";
            Object[] args = new Object[]{seller.getSid(), seller.getMid(), seller.getName(), seller.getSellerId(), seller.getAccountName(),
                    seller.getSellerAccountId(), seller.getRegion(), seller.getCountry(), seller.getHasAdsSetting(), seller.getMarketplaceId(),
                    seller.getStatus()};
            jdbc.update(sql, args);
        }
    }

    @Override
    public List<TSeller> queryAllSellers() {
        String sql = "select * from t_seller";
        return jdbc.query(sql, new TSellerMapper());
    }

    @Override
    public List<TSeller> getUserSellers(String id) {
        String sql = "select * from t_seller where sid in (select seller_id from t_user_seller where user_id = ?)";
        return jdbc.query(sql, new TSellerMapper(), id);
    }

    @Override
    public List<JSONObject> getAllUserSellersTies() {
        String sql = "select us.id as id,u.id as user_id, u.username as user_name, s.sid as seller_id, s.name as seller_name " +
                "from t_user u, t_seller s, t_user_seller us where u.id = us.user_id and s.sid = us.seller_id";
        List<JSONObject> result = jdbc.query(sql, (rs, rowNum) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", rs.getString("id"));
            jsonObject.put("user_id", rs.getString("user_id"));
            jsonObject.put("user_name", rs.getString("user_name"));
            jsonObject.put("seller_id", rs.getString("seller_id"));
            jsonObject.put("seller_name", rs.getString("seller_name"));
            return jsonObject;
        });
        return result;
    }

    @Override
    public void clearUserSellers(String userId) {
        String sql = "delete from t_user_seller where user_id = ?";
        jdbc.update(sql, userId);
    }

    @Override
    public boolean saveNewUserSeller(String userId, String sellerId) {
        String sql = "insert into t_user_seller(user_id, seller_id) values(?, ?)";
        return jdbc.update(sql, userId, sellerId) > 0;
    }

    @Override
    public boolean unbindShop(String userId, String sellerId) {
        String sql = "delete from t_user_seller where user_id = ? and seller_id = ?";
        return jdbc.update(sql, userId, sellerId) > 0;
    }

    @Override
    public List<TSeller> queryShopByDepartmentCode(String code) {
        String sql = "select * from t_seller where sid in (select seller_id from t_user_seller where user_id in (select id from t_user where department_code like ?))";
        return List.of();
    }
}
