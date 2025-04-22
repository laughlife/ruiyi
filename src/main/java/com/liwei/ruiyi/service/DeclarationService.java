package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface DeclarationService {
    PageUtils queryMyDeclaration(PageUtils pageUtils);

    boolean deleteDeclarationById(String id);

    boolean createDeclaration(CDeclaration declaration);

    boolean updateDeclaration(CDeclaration dec);

    CDeclaration queryDeclarationById(String id);

    boolean queren(String id);

    boolean buy(CDeclaration dec);

    JSONArray queryDeclarationLog(String id);

    void uploadDeclaration(String id, String types, String src);

    boolean arrival(String id);

    boolean sendToFba(JSONObject params);
}
