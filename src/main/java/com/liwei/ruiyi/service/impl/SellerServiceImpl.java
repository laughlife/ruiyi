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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                s.put("userId",id);
                s.put("country", seller.getCountry());
                result.add(s);
            }
            return result;
        }
        return List.of();
    }

    @Override
    public List<JSONObject> queryShopToBind(String userId) {
        List<TSeller> sellers = sellerDao.queryAllSellers();
        List<JSONObject> user_seller = sellerDao.getAllUserSellersTies();

        // 如果没有卖家信息，直接返回空列表
        if (sellers == null || sellers.isEmpty()) {
            return List.of();
        }

        // 将 user_seller 转换为 Map，避免重复遍历
        Map<Integer, String> sellerUserMap = new HashMap<>();
        Map<Integer, Boolean> userCheckStatusMap = new HashMap<>();
        for (JSONObject us : user_seller) {
            Integer sellerId = us.getInteger("seller_id");
            Integer currentUserId = us.getInteger("user_id");

            // 拼接绑定信息
            String bind = sellerUserMap.get(sellerId);
            if (bind == null) {
                sellerUserMap.put(sellerId, us.getString("name"));
            } else {
                sellerUserMap.put(sellerId, bind + " | " + us.getString("name"));
            }

            // 记录是否已绑定当前用户
            if (currentUserId.equals(Integer.parseInt(userId))) {
                userCheckStatusMap.put(sellerId, true);
            }
        }

        // 构建结果
        List<JSONObject> result = new ArrayList<>();
        for (TSeller seller : sellers) {
            JSONObject s = new JSONObject();
            s.put("id", seller.getSid());
            s.put("name", seller.getName());
            s.put("country", seller.getCountry());
            s.put("userId", userId);

            // 设置同步状态
            String status = switch (seller.getStatus()) {
                case 0 -> "停止同步";
                case 1 -> "正常";
                case 2 -> "授权异常";
                case 3 -> "欠费停服";
                default -> "未知状态";
            };
            s.put("status", status);

            // 设置绑定信息
            String bind = sellerUserMap.get(seller.getSid());
            if (bind != null) {
                s.put("bind", bind);
            }

            // 设置 checkStatus
            s.put("LAY_CHECKED", userCheckStatusMap.getOrDefault(seller.getSid(), false));

            result.add(s);
        }

        return result;
    }

    @Override
    public boolean bindSeller(String userId, JSONArray array) {
        boolean rs = true;
        sellerDao.clearUserSellers(userId);
        for (int i = 0; i < array.size(); i++) {
            String sellerId = array.getJSONObject(i).getString("id");
            if(!sellerDao.saveNewUserSeller(userId, sellerId)){
                rs = false;
            }
        }
        return rs;
    }

    @Override
    public boolean unbindShop(String userId, String sellerId) {
        return sellerDao.unbindShop(userId, sellerId);
    }
}
