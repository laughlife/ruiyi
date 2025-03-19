package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONArray;
import com.liwei.ruiyi.bo.TAdmin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TAdminService {

    TAdmin queryAdminMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword,String password);

}