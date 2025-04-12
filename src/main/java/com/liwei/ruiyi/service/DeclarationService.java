package com.liwei.ruiyi.service;

import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface DeclarationService {
    PageUtils queryMyDeclaration(PageUtils pageUtils);

    boolean deleteDeclarationById(String id);

    boolean createDeclaration(CDeclaration declaration);

    boolean updateDeclaration(String id, String field, String value);
}
