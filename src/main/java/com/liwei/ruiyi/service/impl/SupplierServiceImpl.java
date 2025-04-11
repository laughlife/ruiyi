package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.SupplierService;
import com.liwei.ruiyi.dao.CSupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("supplierService")
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private CSupplierDao supplierDao;
}
