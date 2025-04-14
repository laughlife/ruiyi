package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.service.SupplierService;
import com.liwei.ruiyi.service.TProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    //get_fee_type
    @Autowired
    private HttpServletRequest request;

    @Autowired
    TProductService tproductService;

    SupplierService supplierService;

    @RequestMapping("/get_product_performance")
    @ResponseBody
    public String getProductPerformance(String start_date, String end_date) {
        JSONObject rj = new JSONObject();
        boolean queryStatus = tproductService.getOrRefreshProductPerformance(start_date, end_date);
        rj.put("status", queryStatus);
        String msg = "费用类型同步成功.";
        rj.put("msg", queryStatus?msg:"费用类型同步失败");
        return rj.toJSONString();
    }




    @RequestMapping("/goProductPage")
    public String goProductPage() {
        List<CSupplier> supplierList = supplierService.queryAllSupplierForSearch();
        request.setAttribute("supplierList", supplierList);
        return "page/c_product/product";
    }
    @RequestMapping("/goCreateProduct")
    public String goCreateProduct() {
        List<CSupplier> supplierList = supplierService.queryAllSupplierForSearch();
        request.setAttribute("supplierList", supplierList);
        return "page/c_product/createProduct";
    }
}
