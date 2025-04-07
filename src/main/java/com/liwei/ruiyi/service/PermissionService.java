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
}
