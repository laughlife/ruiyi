package com.liwei.ruiyi.service;

import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface SupplierService {
    PageUtils querySupplierByPage(PageUtils pageUtils);

    boolean createSupplier(CSupplier supplier);

    boolean updateSupplier(String id, String field, String value);

    boolean deleteSupplierById(String id);
}
