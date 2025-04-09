package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.TUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    JSONObject queryAllBm();

    JSONObject queryMyBm(String departmentCode);

    List<TDepartment> getBmList();

    boolean addBm(TDepartment bm);

    JSONObject deleteDepartment(String bmId);

    List<JSONObject> getBmcyList(Integer id);

    TDepartment getBmById(String id);

    JSONArray queryBmcy(String id);

    JSONArray queryBmcyByKey(String id, String key);

    boolean updateBmcy(String[] newUserIds, String[] removedUserIds, String id);

    boolean deleteBmcyById(String id);

    boolean setLdById(String id);

    boolean updateBm(JSONObject bm);

    List<TUser> getAllDistinctBmcy();

}
