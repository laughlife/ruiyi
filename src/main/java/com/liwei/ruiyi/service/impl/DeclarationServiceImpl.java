package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.DeclarationService;
import com.liwei.ruiyi.dao.CDeclarationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("declarationService")
public class DeclarationServiceImpl implements DeclarationService {
    @Autowired
    private CDeclarationDao declarationDao;
}
