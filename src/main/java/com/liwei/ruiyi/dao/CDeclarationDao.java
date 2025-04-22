package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface CDeclarationDao {
    PageUtils queryMyDeclaration(PageUtils pageUtils);

    boolean delDdeclaration(String id);

    boolean createDeclaration(CDeclaration declaration);

    boolean updateDeclaration(CDeclaration dec);

    CDeclaration queryDeclarationById(String id);

    boolean queren(String id);

    boolean updatePurcacheMsg(CDeclaration dbDec);

    void uploadDeclarationFaPiao(String id, String src);

    void uploadDeclarationTips(String id, String src);

    boolean arrival(String id);

    boolean sendToFba(JSONObject params);
}
