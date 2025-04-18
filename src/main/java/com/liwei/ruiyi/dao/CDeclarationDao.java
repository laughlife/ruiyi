package com.liwei.ruiyi.dao;

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
}
