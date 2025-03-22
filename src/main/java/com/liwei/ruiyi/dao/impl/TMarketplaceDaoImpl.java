package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TMarketplace;
import com.liwei.ruiyi.bo.mapper.TMarketplaceMapper;
import com.liwei.ruiyi.dao.TMarketplaceDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@Repository("marketplaceDao")
public class TMarketplaceDaoImpl implements TMarketplaceDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdate(TMarketplace m) {
        String sql = "select count(0) from t_marketplace where mid = ?";
        int count = jdbc.queryForObject(sql, new Object[]{m.getMid()}, Integer.class);
        if(count>0){
            sql = "update t_marketplace set region = ?,aws_region = ?,country = ?,code = ?,marketplace_id = ? where mid = ?";
            Object[] args = {m.getRegion(),m.getAwsRegion(),m.getCountry(),m.getCode(),m.getMarketplaceId(),m.getMid()};
            jdbc.update(sql, args);
        }else{
            sql = "insert into t_marketplace(mid,region,aws_region,country,code,marketplace_id) values(?,?,?,?,?,?)";
            Object[] args = {m.getMid(),m.getRegion(),m.getAwsRegion(),m.getCountry(),m.getCode(),m.getMarketplaceId()};
            jdbc.update(sql, args);
        }
    }

    @Override
    public List<TMarketplace> findAll() {
        String sql = "select * from t_marketplace";
        return jdbc.query(sql, new TMarketplaceMapper());
    }
}
