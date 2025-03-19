package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TAdmin;
import org.springframework.stereotype.Service;

@Service
public interface TAdminDao {

    TAdmin queryAdminMessage(String username, String password);

    boolean updatePwd(Integer id, String oldPassword, String password);
}