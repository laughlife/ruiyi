package com.liwei.ruiyi.service;

import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface CProductService {
    PageUtils queryProductByPage(PageUtils pageUtils);

    boolean createProduct(CProduct product);

    CProduct queryProductById(String id);
}
