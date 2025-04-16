package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.bo.TUserSeller;
import com.liwei.ruiyi.bo.TWorldState;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TSellerDao;
import com.liwei.ruiyi.dao.TUserDao;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("sellerService")
public class SellerServiceImpl implements SellerService {

    @Autowired
    TSellerDao sellerDao;

    @Autowired
    LingxingService lingxingService;

    @Autowired
    TUserDao userDao;

    @Override
    public boolean saveOrUpdate() {
        boolean returnStatus = true;
        JSONObject data = lingxingService.get(LingxingConfig.seller_list, null);
        if (data != null) {
            //刷新市场表
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
            }
        }
        return returnStatus;
    }

    @Override
    public List<JSONObject> queryAllUserAndShop() {
        List<TUser> users = userDao.queryAllUser();
        return formatUserShop(users);
    }

    private List<JSONObject> formatUserShop(List<TUser> users) {
        List<JSONObject> result = new ArrayList<>();
        for (TUser user : users) {
            JSONObject u = new JSONObject();
            u.put("id", user.getId());
            u.put("name", user.getName());
            u.put("phone", user.getPhone());
            u.put("department", user.getDepartmentName());
            List<TSeller> sellers = sellerDao.getUserSellers(user.getId()+"");
            String shops = "";
            if (sellers != null && sellers.size() > 0) {
                for (int i = 0; i < sellers.size(); i++) {
                    if(i < sellers.size() - 1){
                        shops += sellers.get(i).getName() + " | ";
                    }else{
                        shops += sellers.get(i).getName();
                    }
                }
                u.put("shops", shops);
            }
            result.add(u);
        }
        return result;
    }

    @Override
    public List<JSONObject> queryUserAndShopByDepartmentId(String departmentCode) {
        List<TUser> users = userDao.queryAllUser();
        return formatUserShop(users);
    }

    @Override
    public List<JSONObject> queryShopByUserId(String id) {
        List<TSeller> sellers = sellerDao.getUserSellers(id);
        if (sellers != null && sellers.size() > 0) {
            List<JSONObject> result = new ArrayList<>();
            for (TSeller seller : sellers) {
                JSONObject s = new JSONObject();
                s.put("id", seller.getSid());
                s.put("name", seller.getName());
                s.put("country", seller.getCountry());
                result.add(s);
            }
            return result;
        }
        return List.of();
    }

    @Override
    public List<JSONObject> queryUnbindShop(String userId) {
        List<TSeller> sellers = sellerDao.queryUnbindShop();
        if (sellers != null && sellers.size() > 0) {
            List<JSONObject> result = new ArrayList<>();
            for (TSeller seller : sellers) {
                JSONObject s = new JSONObject();
                s.put("id", seller.getSid());
                s.put("name", seller.getName());
                s.put("country", seller.getCountry());
                s.put("userId", userId);
                //0停止同步,1正常,2授权异常,3欠费停服
                switch (seller.getStatus()) {
                    case 0:
                        s.put("status", "停止同步");
                        break;
                    case 1:
                        s.put("status", "正常");
                        break;
                    case 2:
                        s.put("status", "授权异常");
                        break;
                    case 3:
                        s.put("status", "欠费停服");
                        break;
                }
                result.add(s);
            }
            return result;
        }
        return List.of();
    }
}
