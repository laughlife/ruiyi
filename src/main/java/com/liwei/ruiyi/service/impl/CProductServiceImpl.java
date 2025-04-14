package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.service.CProductService;
import com.liwei.ruiyi.dao.CProductDao;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("cproductService")
public class CProductServiceImpl implements CProductService {
    @Autowired
    private CProductDao cproductDao;

    @Override
    public PageUtils queryProductByPage(PageUtils pageUtils) {
        String imagePath = ReadProUtils.ReadProperties("imageServiceUrl");

        PageUtils page = cproductDao.queryProductByPage(pageUtils);
        List<CProduct> data = page.getData();
        List<JSONObject> proList = new ArrayList<>();
        for (CProduct pro : data) {
            JSONObject j = new JSONObject();
            j.put("id", pro.getId());
            j.put("name", pro.getName());
            j.put("link", pro.getLink());
            if(StringUtils.isNotEmpty(pro.getImageUrl())){
                j.put("image_url", imagePath + pro.getImageUrl());
            }else{
                j.put("image_url", "");
            }
            j.put("supplier_name", pro.getSupplierName());
            j.put("cost_price", pro.getCostPrice());
            j.put("create_time", pro.getCreateTime());
            j.put("update_time", pro.getUpdateTime());
            j.put("unship_quantity", pro.getUnshipQuantity());
            j.put("unship_price", pro.getUnshipPrice());
            j.put("other", pro.getOther());
            proList.add(j);
        }
        page.setData(proList);
        return page;
    }

    @Override
    public boolean createProduct(CProduct product) {
        return cproductDao.createProduct(product);
    }

    @Override
    public CProduct queryProductById(String id) {
        return cproductDao.queryProductById(id);
    }

    @Override
    public boolean updateProduct(CProduct product) {
        return cproductDao.updateProduct(product);
    }

    @Override
    public JSONObject deleteProduct(String id) {
        return cproductDao.deleteProduct(id);
    }
}
