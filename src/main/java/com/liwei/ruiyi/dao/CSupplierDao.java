package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.CSupplier;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CSupplierDao {
    PageUtils querySupplierByPage(PageUtils pageUtils);

    boolean createSupplier(CSupplier supplier);

    boolean updateSupplier(String id, String field, String value);

    boolean deleteSupplierById(String id);

    List<CSupplier> queryAllSupplierForSearch();
}
