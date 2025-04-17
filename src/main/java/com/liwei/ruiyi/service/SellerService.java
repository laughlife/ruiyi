package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellerService {
    boolean saveOrUpdate();

    List<JSONObject> queryAllUserAndShop();

    List<JSONObject> queryUserAndShopByDepartmentId(String departmentCode);

    List<JSONObject> queryShopByUserId(String id);

    List<JSONObject> queryShopToBind(String userId);

    boolean bindSeller(String userId, JSONArray array);

    boolean unbindShop(String userId, String sellerId);
}
