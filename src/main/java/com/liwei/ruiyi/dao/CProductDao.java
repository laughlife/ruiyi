package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface CProductDao {
    PageUtils queryProductByPage(PageUtils pageUtils);

    boolean createProduct(CProduct product);

    CProduct queryProductById(String id);

    boolean updateProduct(CProduct product);

    JSONObject deleteProduct(String id);
}
