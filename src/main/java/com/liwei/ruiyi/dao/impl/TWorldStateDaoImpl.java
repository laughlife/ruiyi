package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TWorldState;
import com.liwei.ruiyi.dao.TWorldStateDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@Repository("worldStateDao")
public class TWorldStateDaoImpl implements TWorldStateDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdate(TWorldState w) {
        String sql = "select count(0) from t_world_state where country_code = ? and code = ?";
        Object[] args = new Object[]{w.getCountryCode(), w.getCode()};
        int count = jdbc.queryForObject(sql, args, Integer.class);
        if(count>0){
            sql = "update t_world_state set mid = ?, state_or_province_name = ? where country_code = ? and code = ?";
            args = new Object[]{w.getMid(), w.getStateOrProvinceName(), w.getCountryCode(), w.getCode()};
            jdbc.update(sql, args);
        }else{
            sql = "insert into t_world_state(mid, state_or_province_name, country_code, code) values(?,?,?,?)";
            args = new Object[]{w.getMid(), w.getStateOrProvinceName(), w.getCountryCode(), w.getCode()};
            jdbc.update(sql, args);
        }
    }
}
