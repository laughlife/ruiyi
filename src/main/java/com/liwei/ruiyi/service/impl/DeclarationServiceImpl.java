package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.bo.CProduct;
import com.liwei.ruiyi.dao.CProductDao;
import com.liwei.ruiyi.service.DeclarationService;
import com.liwei.ruiyi.dao.CDeclarationDao;
import com.liwei.ruiyi.utils.PageUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository("declarationService")
public class DeclarationServiceImpl implements DeclarationService {
    @Autowired
    private CDeclarationDao declarationDao;

    @Autowired
    CProductDao cproductDao;

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
            j.put("shc", declaration.getShc());
            j.put("asin", declaration.getAsin());
            if(StringUtils.isNotEmpty(declaration.getImagePath())) {
                j.put("image_path", imagePath + declaration.getImagePath());
            }else{
                j.put("image_path", "");
            }
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
        //删除的时候把冗余图片也删除掉
        CDeclaration declaration = declarationDao.queryDeclarationById(id);
        boolean delete = declarationDao.delDdeclaration(id);
        if (delete) {
            String imageSavePath = ReadProUtils.ReadProperties("imageSavePath");
            String imagePath = declaration.getImagePath();
            File file = new File(imageSavePath, imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return delete;
    }

    @Override
    public boolean createDeclaration(CDeclaration declaration) {

        return declarationDao.createDeclaration(declaration);
    }

    @Override
    public boolean updateDeclaration(CDeclaration dec) {

        return declarationDao.updateDeclaration(dec);
    }

    @Override
    public CDeclaration queryDeclarationById(String id) {
        return declarationDao.queryDeclarationById(id);
    }

    @Override
    public boolean queren(String id) {
        return declarationDao.queren(id);
    }
}
