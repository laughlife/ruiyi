package com.liwei.ruiyi.config;

public class LingxingConfig {
    //获取 access-token和refresh-token POST
    public static String getTokenPath = "/api/auth-server/oauth/access-token";
    //token续约 POST
    public static String refreshTokenPath = "/api/auth-server/oauth/refresh";
    //查询亚马逊市场列表 GET
    public static String allMarketplace = "/erp/sc/data/seller/allMarketplace";
    //查询得到亚马逊对应国家的地区列表数据 GET
    public static String marketplaceWorldState = "/erp/sc/data/worldState/lists";
    //查询得到企业已授权到领星ERP的全部亚马逊店铺信息 GET
    public static String seller_list = "/erp/sc/data/seller/lists";
    //查询得到亚马逊订单列表 POST
    public static String query_orders = "/erp/sc/data/mws/orders";



}
