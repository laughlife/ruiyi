package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TPermissionDao {
    List<TPermission> getAllPermission();
}
