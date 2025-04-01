package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.dao.TProHistoryDao;
import com.liwei.ruiyi.utils.SqlUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedHashSet;

@Repository("proHistoryDao")
public class TProHistoryDaoImpl implements TProHistoryDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdateProHistory(JSONObject pro_history) {

        try{
            Integer cid = pro_history.getInteger("cid");
            if (cid == null || cid == 0) {
                // 如果 cid 为空或为 0，设置为 NULL
                pro_history.put("cid",null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String sql = "SELECT COUNT(0) FROM t_pro_history WHERE mid = ? AND sid = ? and asin = ? and seller_sku = ? and query_date = ?";
        Object[] args = new Object[]{pro_history.getString("mid"), pro_history.getString("sid"),
                pro_history.getString("asin"), pro_history.getString("seller_sku"), pro_history.getString("query_date")};
        int count = jdbc.queryForObject(sql, args, Integer.class);
        if (count > 0) {
            // 生成 UPDATE 语句
            // 调用示例
            LinkedHashSet<String> updateKeys = SqlUtils.getUpdateKeys(pro_history, "mid", "sid", "asin", "seller_sku", "query_date");
            String updateSql = SqlUtils.generateUpdateSQL(pro_history, "t_pro_history", updateKeys, "mid", "sid", "asin", "seller_sku", "query_date");
            Object[] updateValues = SqlUtils.getUpdateSQLValues(pro_history, updateKeys, "mid", "sid", "asin", "seller_sku", "query_date");
            try {
                jdbc.update(updateSql, updateValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String insertSql = SqlUtils.generateInsertSQL(pro_history, "t_pro_history", "mid", "sid", "asin", "seller_sku", "query_date");
            Object[] insertValues = SqlUtils.getInsertSQLValues(pro_history, "mid", "sid", "asin", "seller_sku", "query_date");
            try {
                jdbc.update(insertSql, insertValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
