package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.service.DeclarationService;
import com.liwei.ruiyi.dao.CDeclarationDao;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("declarationService")
public class DeclarationServiceImpl implements DeclarationService {
    @Autowired
    private CDeclarationDao declarationDao;

    @Override
    public PageUtils queryMyDeclaration(PageUtils pageUtils) {
        String imagePath = ReadProUtils.ReadProperties("imageServiceUrl");

        PageUtils page = declarationDao.queryMyDeclaration(pageUtils);
        List<CDeclaration> data = page.getData();
        List<JSONObject> declarationList = new ArrayList<>();
        for (CDeclaration declaration : data) {
            JSONObject j = new JSONObject();
            j.put("id", declaration.getId());
            j.put("user_id", declaration.getUserId());
            j.put("user_name", declaration.getUserName());
            j.put("user_phone", declaration.getUserPhone());
            j.put("pro_name", declaration.getProName());
            j.put("link", declaration.getLink());
            j.put("asin", declaration.getAsin());
            j.put("image_path", imagePath + declaration.getImagePath());
            j.put("purchase_packages", declaration.getPurchasePackages());
            j.put("per_package_quantity", declaration.getPerPackageQuantity());
            j.put("total_quantity", declaration.getTotalQuantity());
            j.put("other", declaration.getOther());
            j.put("shipped_quantity", declaration.getShippedQuantity());
            j.put("received_quantity", declaration.getReceivedQuantity());
            j.put("declare_time", declaration.getDeclareTime());
            j.put("status", declaration.getStatus());
            declarationList.add(j);
        }
        page.setData(declarationList);
        return page;
    }

    @Override
    public boolean deleteDeclarationById(String id) {
        return declarationDao.declarationDao(id);
    }

    @Override
    public boolean createDeclaration(CDeclaration declaration) {

        return declarationDao.createDeclaration(declaration);
    }

    @Override
    public boolean updateDeclaration(String id, String field, String value) {

        return declarationDao.updateDeclaration(id, field, value);
    }
}
