package com.liwei.ruiyi.dao.impl;

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
        if(count > 0){
            sql = "update t_seller set mid = ?, name = ?, seller_id = ?,account_name = ?, seller_account_id = ?, " +
                    "region = ?, country = ?, has_ads_setting = ?, marketplace_id = ?, status = ? " +
                    "where sid = ?";
            Object[] args = new Object[]{seller.getMid(), seller.getName(), seller.getSellerId(), seller.getAccountName(), seller.getSellerAccountId(),
                    seller.getRegion(), seller.getCountry(), seller.getHasAdsSetting(), seller.getMarketplaceId(), seller.getStatus(),
                    seller.getSid()};
            jdbc.update(sql, args);
        }else{
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
        return jdbc.query(sql,new TSellerMapper());
    }
}
