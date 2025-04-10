package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.dao.TProfitDayDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import com.liwei.ruiyi.utils.SqlUtils;

@Repository("profitDayDao")
public class TProfitDayDaoImpl implements TProfitDayDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdateProfitReport(JSONObject profit) {
        String sql = "SELECT COUNT(0) FROM t_profit_day WHERE sid = ? AND profit_day = ?";
        Object[] args = new Object[]{profit.getString("sid"), profit.getString("profit_day")};

        int count = jdbc.queryForObject(sql, args, Integer.class);

        if (count > 0) {
            // 生成 UPDATE 语句
            // 调用示例
            LinkedHashSet<String> updateKeys = SqlUtils.getUpdateKeys(profit, "sid", "profit_day");
            String updateSql = SqlUtils.generateUpdateSQL(profit, "t_profit_day", updateKeys, "sid", "profit_day");
            Object[] updateValues = SqlUtils.getUpdateSQLValues(profit, updateKeys, "sid", "profit_day");
            try {
                jdbc.update(updateSql, updateValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String insertSql = SqlUtils.generateInsertSQL(profit, "t_profit_day", "sid", "profit_day");
            Object[] insertValues = SqlUtils.getInsertSQLValues(profit,"sid", "profit_day");
            try {
                jdbc.update(insertSql, insertValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
