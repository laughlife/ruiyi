package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.dao.TOrderDao;
import com.liwei.ruiyi.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("orderDao")
public class TOrderDaoImpl implements TOrderDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdateOrders(JSONObject order) {
        order.remove("item_list");
        String amazon_order_id = order.getString("amazon_order_id");

        //order中的所有字段，如果为空或者是空字符串，设置为null
        for (Map.Entry<String, Object> entry : order.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals("")) {
                order.put(entry.getKey(), null);
            }
        }
        // 将格式化后的日期设置回 order 对象
        order.put("purchase_date", ISOTimeToSystemTime(order.getString("purchase_date")));
        order.put("earliest_ship_date", ISOTimeToSystemTime(order.getString("earliest_ship_date")));
        order.put("posted_date_utc", ISOTimeToSystemTime(order.getString("posted_date_utc")));

        String checkSql = "SELECT COUNT(0) FROM t_order WHERE amazon_order_id = ?";
        int count = jdbc.queryForObject(checkSql, new Object[]{order.getString("amazon_order_id")}, Integer.class);

        if (count > 0) {
            // 生成 UPDATE 语句
            String updateSql = generateUpdateSQL(order, "t_order", "amazon_order_id");
            order.put("amazon_order_id",amazon_order_id);
            Object[] updateValues = getUpdateValues(order, "amazon_order_id");
            jdbc.update(updateSql, updateValues);
        } else {
            // 生成 INSERT 语句
            String insertSql = generateInsertSQL(order, "t_order");
            jdbc.update(insertSql, getValues(order));
        }

    }

    private String ISOTimeToSystemTime(String purchaseDate) {
        if (purchaseDate == null) {
            return null;
        }
        Instant instant = Instant.parse(purchaseDate);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        // 将 LocalDateTime 格式化为 MySQL 支持的 datetime 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @Override
    public void saveOrUpdateOrderItems(String orderId, JSONArray items) {
        if (items == null || items.size() == 0) {
            return;
        }
        String sql = "SELECT COUNT(0) FROM t_order_item WHERE id = ?";
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            String id = item.getString("asin") + orderId;
            item.put("id", id);
            item.put("order_id", orderId);
            int count = jdbc.queryForObject(sql, new Object[]{id}, Integer.class);
            if (count > 0) {
                // 生成 UPDATE 语句
                String updateSql = generateUpdateSQL(item, "t_order_item", "id");
                item.put("id", id);
                Object[] updateValues = getUpdateValues(item, "id");
                jdbc.update(updateSql, updateValues);
            } else {
                // 生成 INSERT 语句
                String insertSql = generateInsertSQL(item, "t_order_item");
                jdbc.update(insertSql, getValues(item));
            }
        }
    }

    // 生成 INSERT 语句
    // 生成 INSERT 语句
    private String generateInsertSQL(JSONObject order, String tableName) {
        Set<String> keys = order.keySet();
        String columns = String.join(", ", keys);
        String placeholders = String.join(", ", keys.stream().map(k -> "?").toArray(String[]::new));

        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    // 生成 UPDATE 语句
    private String generateUpdateSQL(JSONObject order, String tableName, String primaryKey) {
        Set<String> keys = order.keySet();
        keys.remove(primaryKey); // 过滤掉主键，避免 SET 里面更新主键

        String setClause = String.join(", ", keys.stream().map(k -> k + " = ?").toArray(String[]::new));
        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + primaryKey + " = ?";
    }

    // 提取 JSON 对象的值，按顺序转换为数组
    private Object[] getValues(JSONObject order) {
        List<Object> values = order.values().stream().toList();
        return values.toArray(new Object[0]);
    }

    // 获取 UPDATE 的值（去掉主键 + 最后追加主键）
    private Object[] getUpdateValues(JSONObject order, String primaryKey) {
        String primaryValue = order.getString(primaryKey);
        List<Object> values = order.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(primaryKey)) // 过滤掉主键
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()); // 使用 collect(Collectors.toList()) 生成可变集合
        // 追加 WHERE 条件的值
        values.add(primaryValue);
        return values.toArray(new Object[0]);
    }
}
