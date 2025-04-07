package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.DepartmentPermissionService;
import com.liwei.ruiyi.dao.TDepartmentPermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("departmentpermissionService")
public class DepartmentPermissionServiceImpl implements DepartmentPermissionService {
    @Autowired
    private TDepartmentPermissionDao departmentpermissionDao;
}
