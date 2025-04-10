package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    JSONObject getAllPermission();

    boolean addRootMenu(String name);

    boolean updatePermission(String id, String field, String value);

    boolean deletePermission(String id);

    boolean addChildMenu(String parentId, String name);


    /**
     * 获取所有部门，用于权限设置左侧的菜单树
     * @return
     */
    JSONArray getAllDepartments();

    /**
     * 根据部门ID获取权限，展示在权限右侧的功能列表，用于设置部门权限。
     * @param departmentId
     * @return
     */
    JSONArray getPermissionByDepartmentId(String departmentId,int isAdmin);
}
