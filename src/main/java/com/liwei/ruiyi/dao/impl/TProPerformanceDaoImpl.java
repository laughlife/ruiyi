package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.dao.TProPerformanceDao;
import com.liwei.ruiyi.utils.SqlUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedHashSet;

@Repository("proPerformanceDao")
public class TProPerformanceDaoImpl implements TProPerformanceDao {

    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdateProPerformance(JSONObject pro) {
        pro.remove("ranking_update_time");

        String sql = "SELECT count(0) FROM t_pro_performance WHERE sid = ? AND query_date = ? and parent_asin = ? and asin = ?";
        Object[] args = new Object[]{pro.getString("sid"), pro.getString("query_date"),
                pro.getString("parent_asin"), pro.getString("asin")};
        int count = jdbc.queryForObject(sql, args, Integer.class);
        if (count > 0) {
            // 生成 UPDATE 语句
            // 调用示例
            LinkedHashSet<String> updateKeys = SqlUtils.getUpdateKeys(pro, "sid", "query_date","parent_asin","asin");
            String updateSql = SqlUtils.generateUpdateSQL(pro, "t_pro_performance", updateKeys, "sid", "query_date","parent_asin","asin");
            Object[] updateValues = SqlUtils.getUpdateSQLValues(pro, updateKeys, "sid", "query_date","parent_asin","asin");
            try {
                jdbc.update(updateSql, updateValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String insertSql = SqlUtils.generateInsertSQL(pro, "t_pro_performance", "sid", "query_date","parent_asin","asin");
            Object[] insertValues = SqlUtils.getInsertSQLValues(pro, "sid", "query_date","parent_asin","asin");
            try {
                jdbc.update(insertSql, insertValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
