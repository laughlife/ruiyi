package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.CProductService;
import com.liwei.ruiyi.dao.CProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("cproductService")
public class CProductServiceImpl implements CProductService {
    @Autowired
    private CProductDao cproductDao;
}
