package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.MarketplaceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.liwei.ruiyi.service.SellerService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    SellerService sellerService;

    @RequestMapping("/seller_list")
    @ResponseBody
    public String seller_list() {
        //查询得到企业已授权到领星ERP的全部亚马逊店铺信息
        JSONObject rj = new JSONObject();
        boolean refreshStatus = sellerService.saveOrUpdate();
        rj.put("status", refreshStatus);
        rj.put("msg", refreshStatus ? "店铺信息刷新成功" : "店铺信息刷新失败");
        return rj.toJSONString();
    }

    @RequestMapping("/goSellerPage")
    public String goSellerPage() {
        //店铺绑定
        return "page/seller/shop";
    }
    @RequestMapping("/goBindSellerPage")
    public String goBindSellerPage(String userId) {
        //店铺绑定
        request.getSession().setAttribute("userId",userId);
        return "page/seller/bindShop";
    }

    @RequestMapping("/queryUser")
    @ResponseBody
    public String queryUser() {
        //查询部门下所有用户
        TUser user = (TUser) request.getSession().getAttribute("user");
        List<JSONObject> userList = new ArrayList<>();
        if (user.getIsAdmin() == 1) {
            //如果是管理员，则查询所有的用户信息
            userList = sellerService.queryAllUserAndShop();
        } else {
            userList = sellerService.queryUserAndShopByDepartmentId(user.getDepartmentId() + "");
        }
        JSONObject rj = new JSONObject();
        rj.put("code", 0);
        rj.put("msg", "操作成功");
        rj.put("count", userList.isEmpty() ? 0 : userList.size());
        rj.put("data", userList);
        return rj.toJSONString();
    }

    @RequestMapping("/queryShop")
    @ResponseBody
    public String queryShop(String id) {
        //查询用户绑定的店铺
        List<JSONObject> userList = sellerService.queryShopByUserId(id);
        JSONObject rj = new JSONObject();
        rj.put("code", 0);
        rj.put("msg", "操作成功");
        rj.put("count", userList.isEmpty() ? 0 : userList.size());
        rj.put("data", userList);
        return rj.toJSONString();
    }

    @RequestMapping("/queryUnbindShop")
    @ResponseBody
    public String queryUnbindShop(String userId) {
        //查询用户绑定的店铺
        List<JSONObject> userList = sellerService.queryUnbindShop(userId);
        JSONObject rj = new JSONObject();
        rj.put("code", 0);
        rj.put("msg", "操作成功");
        rj.put("count", userList.isEmpty() ? 0 : userList.size());
        rj.put("data", userList);
        return rj.toJSONString();
    }


}
