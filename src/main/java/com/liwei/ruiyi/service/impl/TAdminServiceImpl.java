package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TAdmin;
import com.liwei.ruiyi.dao.TAdminDao;
import com.liwei.ruiyi.service.TAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("adminService")
public class TAdminServiceImpl implements TAdminService {
    @Autowired
    private TAdminDao adminDao;


    @Override
    public TAdmin queryAdminMessage(String username, String password) {
        TAdmin admin = adminDao.queryAdminMessage(username, password);
        return admin;
    }

    @Override
    public boolean updatePwd(Integer id, String oldPassword, String password) {
        boolean isUpdate = adminDao.updatePwd(id, oldPassword, password);
        return isUpdate;
    }


}