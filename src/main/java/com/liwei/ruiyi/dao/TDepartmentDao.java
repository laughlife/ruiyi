package com.liwei.ruiyi.dao;

import com.liwei.ruiyi.bo.TDepartment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TDepartmentDao {
    List<TDepartment> getBmList();

    TDepartment getBmById(String id);

    boolean addBm(TDepartment bm);
}
