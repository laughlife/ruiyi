package com.liwei.ruiyi.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class SqlUtils {
    public static String generateInsertSQL(JSONObject data, String tableName, String... primaryKeys) {
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


    public static Object[] getInsertSQLValues(JSONObject data, String... primaryKeys) {
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

    public static String generateUpdateSQL(JSONObject data, String tableName, LinkedHashSet<String> updateKeys, String... primaryKeys) {
        String setClause = updateKeys.stream()
                .map(k -> k + " = ?")
                .collect(Collectors.joining(", "));

        String whereClause = Arrays.stream(primaryKeys)
                .map(pk -> pk + " = ?")
                .collect(Collectors.joining(" AND "));

        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
    }

    public static LinkedHashSet<String> getUpdateKeys(JSONObject data, String... primaryKeys) {
        Set<String> pkSet = new HashSet<>(Arrays.asList(primaryKeys));
        return data.keySet().stream()
                .filter(key -> !pkSet.contains(key))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static Object[] getUpdateSQLValues(JSONObject data, LinkedHashSet<String> updateKeys, String... primaryKeys) {
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
