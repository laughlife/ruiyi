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
        Object[] args = new Object[]{profit.getString("id"), profit.getString("profit_day")};
        int count = jdbc.queryForObject(sql, args, Integer.class);
        //postedDateDayLocale
        if (count > 0) {
            // 生成 UPDATE 语句
            String updateSql = generateUpdateSQL(profit, "t_profit_day", "id", "profit_day");
            Object[] updateValues = getUpdateValues(profit, "id", "profit_day");
            try {
                jdbc.update(updateSql, updateValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String insertSql = generateInsertSQL(profit, "t_profit_day", "id", "profit_day");
            Object[] insertValues = getValues(profit,"id", "profit_day");
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

    private String generateUpdateSQL(JSONObject data, String tableName, String... primaryKeys) {
        List<String> pkList = Arrays.asList(primaryKeys);
        Set<String> updateKeys = data.keySet().stream()
                .filter(key -> !pkList.contains(key))
                .collect(Collectors.toSet());

        String setClause = updateKeys.stream()
                .map(k -> k + " = ?")
                .collect(Collectors.joining(", "));

        String whereClause = Arrays.stream(primaryKeys)
                .map(pk -> pk + " = ?")
                .collect(Collectors.joining(" AND "));

        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
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

    private Object[] getUpdateValues(JSONObject data, String... primaryKeys) {
        List<Object> values = new ArrayList<>();

        // 获取非主键字段值
        for (String key : data.keySet()) {
            if (!Arrays.asList(primaryKeys).contains(key)) {
                values.add(data.get(key));
            }
        }

        // 添加主键字段值（用于 WHERE 子句）
        for (String pk : primaryKeys) {
            values.add(data.get(pk));
        }

        return values.toArray();
    }

}
