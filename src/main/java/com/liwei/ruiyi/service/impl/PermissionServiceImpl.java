package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.service.PermissionService;
import com.liwei.ruiyi.dao.TPermissionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private TPermissionDao permissionDao;

    @Override
    public JSONObject getAllPermission() {
        //现在需要查询所有的菜单信息，暂时不做权限限制
        List<TPermission> permissionList = permissionDao.getAllPermission();
        JSONObject result = new JSONObject();
        result.put("count", permissionList.size());
        result.put("code", 0);
        result.put("data", eachPermission(permissionList));
        return result;
    }

    private JSONArray eachPermission(List<TPermission> permissionList) {
        // 1. 按parentId分组缓存所有权限，提升查询效率
        Map<Integer, List<TPermission>> permissionMap = new HashMap<>();
        for (TPermission permission : permissionList) {
            int parentId = permission.getParentId() != null ? permission.getParentId() : -1;
            permissionMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(permission);
        }

        // 2. 获取所有主菜单（parentId=-1）并按px排序
        List<TPermission> mainMenus = permissionMap.getOrDefault(-1, new ArrayList<>());
        mainMenus.sort(Comparator.comparingInt(TPermission::getPx));

        // 3. 递归构建菜单树
        JSONArray result = new JSONArray();
        for (TPermission mainMenu : mainMenus) {
            result.add(buildMenuTree(mainMenu, permissionMap));
        }
        return result;
    }

    private JSONObject buildMenuTree(TPermission menu, Map<Integer, List<TPermission>> permissionMap) {
        JSONObject jsonMenu = new JSONObject();
        // 添加基础字段
        jsonMenu.put("id", menu.getId());
        jsonMenu.put("name", menu.getName());
        jsonMenu.put("parentId", menu.getParentId());
        jsonMenu.put("dataScope", menu.getDataScope());
        jsonMenu.put("icon", menu.getIcon());
        jsonMenu.put("path", menu.getPath());
        jsonMenu.put("description", menu.getDescription());
        jsonMenu.put("px", menu.getPx());
        jsonMenu.put("isLink", menu.getIsLink());

        // 递归处理子菜单
        List<TPermission> children = permissionMap.getOrDefault(menu.getId(), new ArrayList<>());
        if (!children.isEmpty()) {
            // 子菜单按px升序排序
            children.sort(Comparator.comparingInt(TPermission::getPx));
            JSONArray childArray = new JSONArray();
            for (TPermission child : children) {
                childArray.add(buildMenuTree(child, permissionMap));
            }
            jsonMenu.put("children", childArray);
        } else {
            //jsonMenu.put("child", new JSONArray()); // 保证始终有child字段
        }
        return jsonMenu;
    }


    @Override
    public boolean addRootMenu(String name) {
        return permissionDao.addRootMenu(name);
    }

    @Override
    public boolean updatePermission(String id, String field, String value) {
        return permissionDao.updatePermission(id, field, value);
    }

    @Override
    public boolean deletePermission(String id) {
        return permissionDao.deletePermission(id);
    }

    @Override
    public boolean addChildMenu(String parentId, String name) {
        return permissionDao.addChildMenu(parentId, name);
    }
}
