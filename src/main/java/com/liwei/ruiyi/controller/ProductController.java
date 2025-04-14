package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.service.CProductService;
import com.liwei.ruiyi.service.SupplierService;
import com.liwei.ruiyi.service.TProductService;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    //get_fee_type
    @Autowired
    private HttpServletRequest request;

    @Autowired
    TProductService tproductService;

    @Autowired
    CProductService cproductService;

    @Autowired
    SupplierService supplierService;

    private static final long MAX_SIZE = 1024 * 1024 * 20;

    @RequestMapping("/get_product_performance")
    @ResponseBody
    public String getProductPerformance(String start_date, String end_date) {
        JSONObject rj = new JSONObject();
        boolean queryStatus = tproductService.getOrRefreshProductPerformance(start_date, end_date);
        rj.put("status", queryStatus);
        String msg = "费用类型同步成功.";
        rj.put("msg", queryStatus ? msg : "费用类型同步失败");
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

    @RequestMapping("/goUpdateProduct")
    public String goUpdateProduct(String id) {
        String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl", "conf.properties");
        CProduct product = cproductService.queryProductById(id);
        JSONObject pro = JSONObject.parseObject(JSONObject.toJSONString(product));

        if (StringUtils.isNotBlank(product.getImageUrl())) {
            pro.put("imagePath", imageServiceUrl + product.getImageUrl());
        }

        request.setAttribute("product", pro);
        List<CSupplier> supplierList = supplierService.queryAllSupplierForSearch();
        request.setAttribute("supplierList", supplierList);
        return "page/c_product/updateProduct";
    }

    @RequestMapping("/queryAllProduct")
    @ResponseBody
    public String queryAllProduct() {

        int nowPage = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageUtils pageUtils = new PageUtils(nowPage, limit);
        String key = request.getParameter("key");
        String supplier_id = request.getParameter("supplier_id");
        JSONObject params = new JSONObject();
        params.put("key", key);
        params.put("supplier_id", supplier_id);
        pageUtils.setSearchParams(params);

        PageUtils page = cproductService.queryProductByPage(pageUtils);
        JSONObject returnJson = new JSONObject();
        returnJson.put("code", 0);
        returnJson.put("msg", "操作成功");
        returnJson.put("count", page.getTotal());
        returnJson.put("data", page.getData());

        return returnJson.toJSONString();
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        boolean upload = false;
        String errorMessage = "";
        String savePath = checkFile();
        File saveDir = new File(savePath);
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            errorMessage = "无法创建上传目录。";
        }
        // 检查文件大小
        if (file.getSize() > MAX_SIZE) {
            errorMessage = "上传文件大小超过限制。";
        }
        // 检查扩展名
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            errorMessage = "请选择文件。";
        }
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExt.equals("jpg") && !fileExt.equals("png") && !fileExt.equals("gif") && !fileExt.equals("bmp") && !fileExt.equals("jpeg")) {
            errorMessage = "上传文件扩展名不是jpg|png|gif|bmp|jpeg。";
        }

        String newFileName = com.liwei.ruiyi.utils.StringUtils.getRandomString() + "." + fileExt;
        File uploadedFile = new File(savePath, newFileName);
        JSONObject rj = new JSONObject();
        try {
            file.transferTo(uploadedFile);
            String month = DateUtils.getSystemMonth();
            String src = "/product/" + month + "/" + newFileName;
            String path = ReadProUtils.ReadProperties("imageServiceUrl", "conf.properties") + src;
            rj.put("src", src);
            rj.put("fileName", originalFileName);
            rj.put("imagePath", path);
            upload = true;
        } catch (IOException e) {
            errorMessage = "上传文件失败。";
        }
        rj.put("status", upload);
        rj.put("msg", upload ? originalFileName + "上传成功" : errorMessage);
        return rj.toJSONString();
    }

    private String checkFile() {
        String savePath = ReadProUtils.ReadProperties("productImagePath", "conf.properties");
        File tempFile = new File(savePath);
        String month = DateUtils.getSystemMonth();
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        File file = new File(savePath, month);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    @RequestMapping("/createProduct")
    @ResponseBody
    public String createProduct(CProduct product) {
        boolean status = cproductService.createProduct(product);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "操作成功" : "操作失败");//确认
        return rj.toJSONString();
    }

    @RequestMapping("/updateProduct")
    @ResponseBody
    public String updateProduct(CProduct product) {
        TUser user = (TUser) request.getSession().getAttribute("user");
        String history = "修改人：" + user.getUsername() + "，修改时间：" + DateUtils.getSystemDate();
        product.setHistory(history);

        boolean status = cproductService.updateProduct(product);
        JSONObject rj = new JSONObject();
        rj.put("status", status);
        rj.put("msg", status ? "操作成功" : "操作失败");//确认
        return rj.toJSONString();
    }
    @RequestMapping("/deleteProduct")
    @ResponseBody
    public String deleteProduct(String id) {
        JSONObject rj = cproductService.deleteProduct(id);
        return rj.toJSONString();
    }
}
