package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.service.DepartmentService;
import com.liwei.ruiyi.dao.TDepartmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private TDepartmentDao departmentDao;
}
