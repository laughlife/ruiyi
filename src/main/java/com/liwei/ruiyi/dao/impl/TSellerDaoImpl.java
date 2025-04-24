package com.liwei.ruiyi.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.mapper.TSellerMapper;
import com.liwei.ruiyi.dao.TSellerDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository("sellerDao")
public class TSellerDaoImpl implements TSellerDao {
    @Autowired
    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Override
    public void saveOrUpdate(TSeller seller) {
        String sql = "select count(0) from t_seller where sid = ?";
        int count = jdbc.queryForObject(sql, new Object[]{seller.getSellerId()}, Integer.class);
        if (count > 0) {
            sql = "update t_seller set mid = ?, name = ?, seller_id = ?,account_name = ?, seller_account_id = ?, " +
                    "region = ?, country = ?, has_ads_setting = ?, marketplace_id = ?, status = ? " +
                    "where sid = ?";
            Object[] args = new Object[]{seller.getMid(), seller.getName(), seller.getSellerId(), seller.getAccountName(), seller.getSellerAccountId(),
                    seller.getRegion(), seller.getCountry(), seller.getHasAdsSetting(), seller.getMarketplaceId(), seller.getStatus(),
                    seller.getSid()};
            jdbc.update(sql, args);
        } else {
            sql = "insert into t_seller(sid, mid, name, seller_id, account_name," +
                    "seller_account_id, region, country, has_ads_setting, marketplace_id, " +
                    "`status`) values(?, ?, ?, ?, ?," +
                    " ?, ?, ?, ?, ?," +
                    " ?)";
            Object[] args = new Object[]{seller.getSid(), seller.getMid(), seller.getName(), seller.getSellerId(), seller.getAccountName(),
                    seller.getSellerAccountId(), seller.getRegion(), seller.getCountry(), seller.getHasAdsSetting(), seller.getMarketplaceId(),
                    seller.getStatus()};
            jdbc.update(sql, args);
        }
    }

    @Override
    public List<TSeller> queryAllSellers() {
        String sql = "select * from t_seller";
        return jdbc.query(sql, new TSellerMapper());
    }

    @Override
    public List<TSeller> getUserSellers(String id) {
        String sql = "select * from t_seller where sid in (select seller_id from t_user_seller where user_id = ?)";
        return jdbc.query(sql, new TSellerMapper(), id);
    }

    @Override
    public List<JSONObject> getAllUserSellersTies() {
        String sql = "select us.id as id,u.id as user_id, u.username as user_name, s.sid as seller_id, s.name as seller_name " +
                "from t_user u, t_seller s, t_user_seller us where u.id = us.user_id and s.sid = us.seller_id";
        List<JSONObject> result = jdbc.query(sql, (rs, rowNum) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", rs.getString("id"));
            jsonObject.put("user_id", rs.getString("user_id"));
            jsonObject.put("user_name", rs.getString("user_name"));
            jsonObject.put("seller_id", rs.getString("seller_id"));
            jsonObject.put("seller_name", rs.getString("seller_name"));
            return jsonObject;
        });
        return result;
    }


    @Override
    public boolean refreshUserSeller(String userId, JSONArray array, JSONArray notCheckArray) {
        try {
            // 1. 处理新增绑定关系（array 非空时执行）
            if (array != null && !array.isEmpty()) {
                // 将 JSONArray 转换为 List<String>
                List<String> sellerIds = array.toJavaList(String.class);
                // 查询已存在的关联关系（避免重复插入）
                String selectSql = buildInClauseSql(
                        "SELECT seller_id FROM t_user_seller WHERE user_id = ? AND seller_id IN ",
                        sellerIds.size()
                );
                List<String> existingSellerIds = jdbc.queryForList(
                        selectSql,
                        String.class,
                        buildParams(userId, sellerIds)
                );

                // 过滤需要插入的 sellerId
                List<String> toInsert = sellerIds.stream()
                        .filter(id -> !existingSellerIds.contains(id))
                        .collect(Collectors.toList());

                // 执行批量插入
                if (!toInsert.isEmpty()) {
                    String insertSql = "INSERT INTO t_user_seller(user_id, seller_id) VALUES (?, ?)";
                    for (String sellerId : toInsert) {
                        int rows = jdbc.update(insertSql, userId, sellerId);
                        if (rows <= 0) {
                            return false; // 插入失败
                        }
                    }
                }
            }

            // 2. 处理解除绑定关系（notCheckArray 非空时执行）
            if (notCheckArray != null && !notCheckArray.isEmpty()) {
                // 将 JSONArray 转换为 List<String>
                List<String> notCheckIds = notCheckArray.toJavaList(String.class);

                // 构建删除 SQL
                String deleteSql = buildInClauseSql(
                        "DELETE FROM t_user_seller WHERE user_id = ? AND seller_id IN ",
                        notCheckIds.size()
                );

                // 执行删除
                int rows = jdbc.update(
                        deleteSql,
                        buildParams(userId, notCheckIds)
                );
                // 无需检查删除行数（即使没有匹配数据也视为成功）
            }

            return true;
        } catch (Exception e) {
            // 日志记录异常（实际项目需替换为日志框架）
            e.printStackTrace();
            return false;
        }
    }

    // 辅助方法：构建 IN 子句的 SQL（防止 SQL 注入）
    private String buildInClauseSql(String baseSql, int paramCount) {
        return baseSql + "(" + String.join(",", Collections.nCopies(paramCount, "?")) + ")";
    }

    // 辅助方法：构建参数数组（userId + 列表参数）
    private Object[] buildParams(String userId, List<String> ids) {
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.addAll(ids);
        return params.toArray();
    }

    @Override
    public boolean unbindShop(String userId, String sellerId) {
        String sql = "delete from t_user_seller where user_id = ? and seller_id = ?";
        return jdbc.update(sql, userId, sellerId) > 0;
    }

    @Override
    public List<TSeller> queryShopByDepartmentCode(String code) {
        String sql = "select * from t_seller where sid in (select seller_id from t_user_seller where user_id in (select id from t_user where department_code like ?))";
        try {
            return jdbc.query(sql, new TSellerMapper(), "%" + code + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<TSeller> getOwnSellerList(String s) {
        try {
            String sql = "select * from t_seller where sid in (select seller_id from t_user_seller where user_id = ?)";
            return jdbc.query(sql, new TSellerMapper(), s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public PageUtils queryAllSellersByPage(PageUtils page) {
        JSONObject params = page.getSearchParams();
        String name = params.getString("name");

        String sql = "select count(0) from t_seller where 1 = ?";
        String querySql = "select * from t_seller where 1 = ? ";
        List<Object> args = new ArrayList<>();
        args.add(1);
        if (StringUtils.isNotBlank(name)) {
            name = "%" + name.trim() + "%";
            sql += " and  name like ?";
            querySql += " and  name like ?";
            args.add(name);
        }
        int count = jdbc.queryForObject(sql, Integer.class, args.toArray());
        page.setTotal(count);
        querySql += " limit ?,?";
        args.add(page.getPageStart());
        args.add(page.getLimit());
        List<TSeller> supplierList = jdbc.query(querySql, new TSellerMapper(), args.toArray());
        page.setData(supplierList);

        return page;
    }
}
