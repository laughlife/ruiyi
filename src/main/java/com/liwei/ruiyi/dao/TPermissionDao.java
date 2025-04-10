package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TPermissionDao {
    List<TPermission> getAllPermission();

    List<TPermission> getPermissionsByDepartmentId(Integer departmentId);

    boolean addRootMenu(String name);

    boolean updatePermission(String id, String field, String value);

    boolean deletePermission(String id);

    boolean addChildMenu(String parentId, String name);

}
