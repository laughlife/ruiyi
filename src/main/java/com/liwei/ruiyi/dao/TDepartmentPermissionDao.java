package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TDepartmentPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TDepartmentPermissionDao {
    /**
     * 根据部门ID获取对应的权限列表
     * @param departmentId
     * @return
     */
    List<TDepartmentPermission> getPermissionByDepartmentId(String departmentId);

    boolean updatePermission(Integer departmentId, List<Integer> permissionIds);
}
