package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.dao.TProfitDayDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

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
        if(profit.getString("sid").equals("4462")){
            System.out.println(profit);
        }
        int count = jdbc.queryForObject(sql, args, Integer.class);
        //minPostedDateDayLocale
        if (count > 0) {
            // 生成 UPDATE 语句
            // 调用示例
            LinkedHashSet<String> updateKeys = getUpdateKeys(profit, "sid", "profit_day");
            String updateSql = generateUpdateSQL(profit, "t_profit_day", updateKeys, "sid", "profit_day");
            Object[] updateValues = getUpdateValues(profit, updateKeys, "sid", "profit_day");
            try {
                jdbc.update(updateSql, updateValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String insertSql = generateInsertSQL(profit, "t_profit_day", "sid", "profit_day");
            Object[] insertValues = getValues(profit,"sid", "profit_day");
            try {
                jdbc.update(insertSql, insertValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String generateInsertSQL(JSONObject data, String tableName, String... primaryKeys) {
        Set<String> keys = data.keySet();
        String columns = String.join(", ", keys);
        String placeholders = String.join(", ", Collections.nCopies(keys.size(), "?"));

        // 生成 ON DUPLICATE KEY UPDATE 子句
        List<String> pkList = Arrays.asList(primaryKeys);
        String updateClause = keys.stream()
                .filter(key -> !pkList.contains(key)) // 过滤掉主键
                .map(key -> key + " = ?") // 使用占位符
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")"
                + (updateClause.isEmpty() ? "" : " ON DUPLICATE KEY UPDATE " + updateClause);
    }


    private Object[] getValues(JSONObject data, String... primaryKeys) {
        List<Object> values = new ArrayList<>(data.values()); // 先添加 INSERT 部分的值

        // 计算需要更新的字段（排除主键）
        Set<String> pkSet = new HashSet<>(Arrays.asList(primaryKeys));
        data.forEach((key, value) -> {
            if (!pkSet.contains(key)) {
                values.add(value); // 追加 ON DUPLICATE KEY UPDATE 的值
            }
        });

        return values.toArray();
    }

    private String generateUpdateSQL(JSONObject data, String tableName, LinkedHashSet<String> updateKeys, String... primaryKeys) {
        String setClause = updateKeys.stream()
                .map(k -> k + " = ?")
                .collect(Collectors.joining(", "));

        String whereClause = Arrays.stream(primaryKeys)
                .map(pk -> pk + " = ?")
                .collect(Collectors.joining(" AND "));

        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
    }

    private LinkedHashSet<String> getUpdateKeys(JSONObject data, String... primaryKeys) {
        Set<String> pkSet = new HashSet<>(Arrays.asList(primaryKeys));
        return data.keySet().stream()
                .filter(key -> !pkSet.contains(key))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Object[] getUpdateValues(JSONObject data, LinkedHashSet<String> updateKeys, String... primaryKeys) {
        List<Object> values = new ArrayList<>();
        for (String key : updateKeys) {
            values.add(data.get(key));
        }
        for (String pk : primaryKeys) {
            values.add(data.get(pk));
        }
        return values.toArray();
    }

}
