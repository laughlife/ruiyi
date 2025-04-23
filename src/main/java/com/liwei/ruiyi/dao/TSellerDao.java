package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import java.util.List;

import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface TSellerDao {
    void saveOrUpdate(TSeller seller);

    List<TSeller> queryAllSellers();

    List<TSeller> getUserSellers(String id);

    List<JSONObject> getAllUserSellersTies();

    void clearUserSellers(String userId);

    boolean saveNewUserSeller(String userId, String sellerId);

    boolean unbindShop(String userId, String sellerId);

    List<TSeller> queryShopByDepartmentCode(String code);

    List<TSeller> getOwnSellerList(String s);

    PageUtils queryAllSellersByPage(PageUtils page);
}
