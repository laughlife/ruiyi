package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TCurrencyInfo;
import com.liwei.ruiyi.bo.TExchangeRate;
import com.liwei.ruiyi.bo.mapper.TCurrencyInfoMapper;
import com.liwei.ruiyi.dao.TCurrencyDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository("currencyDao")
public class TCurrencyDaoImpl implements TCurrencyDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public List<TCurrencyInfo> getCurrencyInfo() {
        String sql = "select * from t_currency_info where is_active = 1";
        return jdbc.query(sql,new TCurrencyInfoMapper());
    }

    @Override
    public void saveOrUpdateRate(TExchangeRate rate) {
        String sql = "select count(0) from t_exchange_rate where base_currency = ? and target_currency = ? and `date` = ?";
        Object[] args = new Object[]{rate.getBaseCurrency(),rate.getTargetCurrency(),rate.getDate()};
        int count = jdbc.queryForObject(sql,args,Integer.class);
        if(count > 0) {
            sql = "update t_exchange_rate set official_rate = ?,custom_rate=? where base_currency = ? and target_currency = ? and `date` = ?";
            args = new Object[]{rate.getOfficialRate(),rate.getCustomRate(), rate.getBaseCurrency(),
                    rate.getTargetCurrency(), rate.getDate()};
            jdbc.update(sql, args);
        }else{
            sql = "insert into t_exchange_rate(base_currency,target_currency,`date`,official_rate) values(?,?,?,?)";
            args = new Object[]{rate.getBaseCurrency(), rate.getTargetCurrency(), rate.getDate(), rate.getOfficialRate()};
            jdbc.update(sql, args);
        }
    }

    @Override
    public boolean isGetCurrency() {
        String sql = "select count(0) from t_exchange_rate where `date` = ?";
        int count = jdbc.queryForObject(sql, Integer.class);
        return false;
    }
}
