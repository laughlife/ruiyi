package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.service.SupplierService;
import com.liwei.ruiyi.utils.PageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    HttpServletRequest request;

    @Autowired
    SupplierService supplierService;


    @RequestMapping("/goSupplier")
    public String go_supplier() {
        return "page/c_supplier/supplier";
    }

    @RequestMapping("/goCreateSupplier")
    public String goCreateSupplier() {
        return "page/c_supplier/create_supplier";
    }

    @RequestMapping("/queryAllSupplier")
    @ResponseBody
    public String queryAllSupplier() {

        int nowPage = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageUtils pageUtils = new PageUtils(nowPage, limit);
        String key = request.getParameter("key");
        JSONObject params = new JSONObject();
        params.put("key", key);
        pageUtils.setSearchParams(params);

        PageUtils page = supplierService.querySupplierByPage(pageUtils);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("count", page.getTotal());
        returnJson.put("data", page.getData());

        return returnJson.toJSONString();
    }

    @RequestMapping("/deleteSupplier")
    @ResponseBody
    public String deleteSupplier(String id) {
        JSONObject returnJson = new JSONObject();
        return returnJson.toJSONString();
    }

    @RequestMapping("/createSupplier")
    @ResponseBody
    public String createSupplier(CSupplier supplier) {
        boolean status = supplierService.createSupplier(supplier);
        JSONObject returnJson = new JSONObject();
        returnJson.put("status", status);
        returnJson.put("icon", status ? 1 : 2);
        returnJson.put("msg", status ? "操作成功" : "操作失败");
        return returnJson.toJSONString();
    }

    @RequestMapping("/updateSupplier")
    @ResponseBody
    public String updateSupplier(String id, String field, String value) {
        JSONObject json = new JSONObject();
        boolean isSuccess = supplierService.updateSupplier(id, field, value);
        json.put("status", isSuccess);
        json.put("msg", isSuccess ? "数据修改成功。" : "数据修改失败，请联系开发人员查找失败原因。");
        return json.toJSONString();
    }
}
