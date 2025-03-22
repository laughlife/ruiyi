package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.TWorldState;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TSellerDao;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("sellerService")
public class SellerServiceImpl implements SellerService {

    @Autowired
    TSellerDao sellerDao;

    @Autowired
    LingxingService lingxingService;

    @Override
    public boolean saveOrUpdate() {
        boolean returnStatus = true;
        JSONObject data = lingxingService.get(LingxingConfig.seller_list, null);
        if (data != null) {
            //刷新市场表
            System.out.println(data.toString());
            if (data.getString("code").equals("0")) {
                JSONArray sellerList = data.getJSONArray("data");
                for (Object obj : sellerList) {
                    JSONObject json = (JSONObject) obj;
                    TSeller seller = new TSeller();
                    seller.setSid(json.getInteger("sid"));
                    seller.setMid(json.getInteger("mid"));
                    seller.setName(json.getString("name"));
                    seller.setSellerId(json.getString("seller_id"));
                    seller.setAccountName(json.getString("account_name"));
                    seller.setSellerAccountId(json.getInteger("seller_account_id"));
                    seller.setRegion(json.getString("region"));
                    seller.setCountry(json.getString("country"));
                    seller.setHasAdsSetting(json.getInteger("has_ads_setting"));
                    seller.setMarketplaceId(json.getString("marketplace_id"));
                    seller.setStatus(json.getInteger("status"));
                    sellerDao.saveOrUpdate(seller);
                }
            } else {
                returnStatus = false;
                System.out.println(data.toString());
            }
        }
        return returnStatus;
    }

}
