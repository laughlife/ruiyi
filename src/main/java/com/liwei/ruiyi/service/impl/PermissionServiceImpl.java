package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.PermissionService;
import com.liwei.ruiyi.dao.TPermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private TPermissionDao permissionDao;
}
