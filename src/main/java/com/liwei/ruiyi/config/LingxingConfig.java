package com.liwei.ruiyi.config;

public class LingxingConfig {
    //获取 access-token和refresh-token POST
    public static String getTokenPath = "/api/auth-server/oauth/access-token";
    //token续约 POST
    public static String refreshTokenPath = "/api/auth-server/oauth/refresh";
    //查询亚马逊市场列表 GET
    public static String allMarketplace = "/erp/sc/data/seller/allMarketplace";
}
