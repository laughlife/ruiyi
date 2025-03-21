package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TMarketplace;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TMarketplaceDao;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("marketplaceService")
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired
    LingxingService lingxingService;

    @Autowired
    TMarketplaceDao marketplaceDao;

    @Override
    public boolean refreshMarketplace() {
        //查询得到亚马逊所有市场列表数据
        JSONObject args = new JSONObject();
        JSONObject data = lingxingService.get(LingxingConfig.allMarketplace,args);

        if (data != null) {
            //刷新市场表
            if(data.getString("code").equals("0")){
                JSONArray marketplaceList = data.getJSONArray("data");
                for (Object obj:marketplaceList) {
                    JSONObject json = (JSONObject) obj;
                    //判断市场是否存在
                    TMarketplace m = new TMarketplace();
                    m.setMid(json.getInteger("mid"));
                    m.setRegion(json.getString("region"));
                    m.setAwsRegion(json.getString("aws_region"));
                    m.setCountry(json.getString("country"));
                    m.setCode(json.getString("code"));
                    m.setMarketplaceId(json.getString("marketplace_id"));
                    marketplaceDao.saveOrUpdate(m);
                }
                return true;
            }else{
                System.out.println(data.toString());
                return false;
            }
        }
        return false;
    }
}
