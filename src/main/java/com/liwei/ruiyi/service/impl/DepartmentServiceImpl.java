package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.dao.TUserDao;
import com.liwei.ruiyi.service.DepartmentService;
import com.liwei.ruiyi.dao.TDepartmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private TDepartmentDao departmentDao;

    @Autowired
    TUserDao userDao;

    @Override
    public JSONObject queryAllBm() {
        List<TDepartment> bmList = departmentDao.getBmList();
        JSONObject result = new JSONObject();
        result.put("count", bmList.size());
        result.put("code", 0);
        result.put("data", eachPermission(bmList));
        return result;
    }

    @Override
    public JSONObject queryMyBm(String departmentCode) {
        List<TDepartment> bmList = departmentDao.getDepartmentsByCode(departmentCode);
        JSONObject result = new JSONObject();
        result.put("count", bmList.size());
        result.put("code", 0);
        result.put("data", eachPermission(bmList));
        return result;
    }

    @Override
    public List<TDepartment> getBmList() {
        return departmentDao.getBmList();
    }

    private JSONArray eachPermission(List<TDepartment> permissionList) {
        // 1. 按parentId分组缓存所有权限，提升查询效率
        Map<Integer, List<TDepartment>> permissionMap = new HashMap<>();
        for (TDepartment permission : permissionList) {
            int parentId = permission.getParentId() != null ? permission.getParentId() : -1;
            permissionMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(permission);
        }

        // 2. 获取所有主菜单（parentId=-1）并按px排序
        List<TDepartment> mainMenus = permissionMap.getOrDefault(-1, new ArrayList<>());
        mainMenus.sort(Comparator.comparingInt(TDepartment::getPx));

        // 3. 递归构建菜单树
        JSONArray result = new JSONArray();
        for (TDepartment mainMenu : mainMenus) {
            result.add(buildMenuTree(mainMenu, permissionMap));
        }
        return result;
    }

    private JSONObject buildMenuTree(TDepartment menu, Map<Integer, List<TDepartment>> permissionMap) {
        JSONObject jsonMenu = new JSONObject();
        // 添加基础字段
        jsonMenu.put("id", menu.getId());
        jsonMenu.put("name", menu.getName());
        jsonMenu.put("code", menu.getCode());
        jsonMenu.put("level", menu.getLevel());
        jsonMenu.put("parentId", menu.getParentId());
        jsonMenu.put("path", menu.getPath());
        jsonMenu.put("px", menu.getPx());
        jsonMenu.put("icon", menu.getIcon());
        jsonMenu.put("description", menu.getDescription());

        // 递归处理子菜单
        List<TDepartment> children = permissionMap.getOrDefault(menu.getId(), new ArrayList<>());
        if (!children.isEmpty()) {
            // 子菜单按px升序排序
            children.sort(Comparator.comparingInt(TDepartment::getPx));
            JSONArray childArray = new JSONArray();
            for (TDepartment child : children) {
                childArray.add(buildMenuTree(child, permissionMap));
            }
            jsonMenu.put("children", childArray);
        }
        return jsonMenu;
    }

    @Override
    public boolean addBm(TDepartment bm) {
        //解析部门信息，部门信息分为一级部门和非一级部门
        //一级部门的parentId为-1，需要添加的信息为：px,icon,level,path
        //path等于code，level为1,px默认为1，顺序不对后期修改
        //icon:   1个人fa-solid fa-user   2个人fa-solid fa-user-group 3个人fa-solid fa-users
        if (bm.getParentId() == -1) {
            bm.setPx(1);
            bm.setIcon("fa-solid fa-user");
            bm.setLevel(1);
            bm.setPath("/" + bm.getCode() + "/");
        } else {
            TDepartment parentBm = departmentDao.getBmById(String.valueOf(bm.getParentId()));
            bm.setPx(1);
            if (parentBm.getLevel() == 1) {
                bm.setLevel(2);
                bm.setIcon("fa-solid fa-user-group");
            } else if (parentBm.getLevel() == 2) {
                bm.setLevel(3);
                bm.setIcon("fa-solid fa-users");
            } else {
                return false;
            }
            bm.setPath(parentBm.getPath() + bm.getCode() + "/");
            bm.setCode(parentBm.getCode() + "-" + bm.getCode());
        }
        //非一级部门的parentId为一级部门的id，需要添加的信息为：code,px,icon,level,path
        return departmentDao.addBm(bm);
    }

    @Override
    public JSONObject deleteDepartment(String bmId) {
        boolean checkCouldDelete = departmentDao.checkCouldDelete(bmId);
        JSONObject returnJson = new JSONObject();
        if (checkCouldDelete) {
            returnJson.put("status", false);
            returnJson.put("msg", "部门信息删除失败，该部门下有子级部门或存在成员信息。");
            return returnJson;
        }else{
            boolean isDelete = departmentDao.deleteDepartmentById(bmId);
            returnJson.put("status", isDelete);
            returnJson.put("msg", isDelete?"删除成功。":"删除失败，请联系开发人员检查错误原因。");
        }
        return returnJson;
    }

    @Override
    public List<JSONObject> getBmcyList(Integer id) {
        TDepartment bm = departmentDao.getBmById(String.valueOf(id));
        List<TUser> userList = userDao.getBmcyList(bm.getCode());
        List<JSONObject> result = new ArrayList<>();
        for (TUser user : userList) {
            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put("username", user.getUsername());
            userJson.put("name", user.getName());
            userJson.put("phone", user.getPhone());
            Integer departmentId = user.getDepartmentId();
            if (departmentId != null && departmentId > 0) {
                TDepartment department = departmentDao.getBmById(String.valueOf(departmentId));
                userJson.put("department", department.getName());
            }else{
                userJson.put("department", "");
            }
            if(user.getIsAdmin() == 1){
                userJson.put("is_admin", "是");
            }else{
                userJson.put("is_admin", "否");
            }
            if(user.getIsLadder() == 1){
                userJson.put("is_ladder", "是");
            }else{
                userJson.put("is_ladder", "否");
            }
            if(user.getIsBan() == 1){
                userJson.put("is_ban", "否");
            }else{
                userJson.put("is_ban", "是");
            }
            result.add(userJson);
        }
        return result;
    }

    @Override
    public TDepartment getBmById(String id) {
        return departmentDao.getBmById(id);
    }

    @Override
    public JSONArray queryBmcy(String id) {
        return null;
    }

    @Override
    public JSONArray queryBmcyByKey(String id, String key) {
        return null;
    }

    @Override
    public boolean updateBmcy(String[] newUserIds, String[] removedUserIds, String id) {
        return false;
    }

    @Override
    public boolean deleteBmcyById(String id) {
        return false;
    }

    @Override
    public boolean setLdById(String id) {
        return false;
    }

    @Override
    public boolean updateBm(JSONObject bm) {
        return departmentDao.updateBm(bm);
    }

    @Override
    public List<TUser> getAllDistinctBmcy() {
        return List.of();
    }
}
