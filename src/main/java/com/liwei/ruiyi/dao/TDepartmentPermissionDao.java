package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TDepartmentPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TDepartmentPermissionDao {
    List<TDepartmentPermission> getPermissionByDepartmentId(String departmentId);
}
