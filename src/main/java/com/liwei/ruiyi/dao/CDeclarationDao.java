package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.CDeclaration;
import com.liwei.ruiyi.utils.PageUtils;
import org.springframework.stereotype.Service;

@Service
public interface CDeclarationDao {
    PageUtils queryMyDeclaration(PageUtils pageUtils);

    boolean declarationDao(String id);

    boolean createDeclaration(CDeclaration declaration);

    boolean updateDeclaration(String id, String field, String value);
}
