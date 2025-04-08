package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TDepartment;
import com.liwei.ruiyi.bo.mapper.TDepartmentMapper;
import com.liwei.ruiyi.dao.TDepartmentDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository("departmentDao")
public class TDepartmentDaoImpl implements TDepartmentDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<TDepartment> getBmList() {
        String sql = "select * from t_department";
        return jdbc.query(sql,new TDepartmentMapper());
    }

    @Override
    public TDepartment getBmById(String id) {
        String sql = "select * from t_department where id = ?";
        try {
            return jdbc.queryForObject(sql, new TDepartmentMapper(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addBm(TDepartment bm) {
        String sql = "insert into t_department(name,code,parent_id,level,path,px,icon,description) values(?,?,?,?,?,?,?,?)";
        Object[] args = {bm.getName(), bm.getCode(), bm.getParentId(), bm.getLevel(), bm.getPath(), bm.getPx(), bm.getIcon(), bm.getDescription()};
        try {
            return jdbc.update(sql, args) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
