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
    //查询亚马逊订单详情 POST
    public static String order_detail = "/erp/sc/data/mws/orderDetail";

    //查询店铺利润日报表
    public static String profit_report = "/bd/profit/report/open/report/seller/list";

    //查询费用类型列表-暂未用到
    public static String fee_type = "/bd/fee/management/open/feeManagement/otherFee/type";
    //查询费用类型明细表-暂未用到
    public static String fee_type_list = "/bd/fee/management/open/feeManagement/otherFee/list";
    //查询产品表现
    public static String get_product_performance = "/bd/productPerformance/openApi/asinList";




}
