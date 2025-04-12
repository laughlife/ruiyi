package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.service.SupplierService;
import com.liwei.ruiyi.dao.CSupplierDao;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("supplierService")
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private CSupplierDao supplierDao;

    @Override
    public PageUtils querySupplierByPage(PageUtils pageUtils) {

        return supplierDao.querySupplierByPage(pageUtils);
    }

    @Override
    public boolean createSupplier(CSupplier supplier) {
        return supplierDao.createSupplier(supplier);
    }

    @Override
    public boolean updateSupplier(String id, String field, String value) {
        return supplierDao.updateSupplier(id, field, value);
    }

    @Override
    public boolean deleteSupplierById(String id) {
        return supplierDao.deleteSupplierById(id);
    }
}
