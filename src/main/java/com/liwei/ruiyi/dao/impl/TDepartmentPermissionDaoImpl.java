package com.liwei.ruiyi.dao.impl;

import com.liwei.ruiyi.bo.TDepartmentPermission;
import com.liwei.ruiyi.bo.TPermission;
import com.liwei.ruiyi.bo.mapper.TDepartmentPermissionMapper;
import com.liwei.ruiyi.bo.mapper.TPermissionMapper;
import com.liwei.ruiyi.dao.TDepartmentPermissionDao;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("departmentPermissionDao")
public class TDepartmentPermissionDaoImpl implements TDepartmentPermissionDao {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<TDepartmentPermission> getPermissionByDepartmentId(String departmentId) {
        String sql = "select * from t_permission";
        List<TPermission> permissionList = jdbc.query(sql, new TPermissionMapper());
        int count = 0;
        //先过滤一遍,看哪个权限没有设置上去
        for (TPermission permission : permissionList) {
            sql = "select count(0) from t_department_permission where department_id = ? and permission_id = ?";
            count = jdbc.queryForObject(sql, Integer.class, departmentId, permission.getId());
            if(count == 0){
                sql = "insert into t_department_permission(department_id, permission_id, has) values(?,?,?)";
                jdbc.update(sql, departmentId, permission.getId(), 0);
            }
        }
        //再返回对应的权限关系信息
        sql = "select * from t_department_permission where department_id = ?";
        return jdbc.query(sql, new TDepartmentPermissionMapper(), departmentId);
    }

    @Override
    public boolean updatePermission(Integer departmentId, List<Integer> permissionIds) {
        String sql = "update t_department_permission set has = 0 where department_id = ?";
        jdbc.update(sql, departmentId);
        //批量更新
        if (permissionIds == null || permissionIds.isEmpty()) {
            return true; // 如果没有需要更新的权限，则直接返回
        }

        // 动态构造 SQL，占位符 ? 的个数要与 permissionIds 的个数一致
        String inSql = String.join(",", Collections.nCopies(permissionIds.size(), "?"));
        sql = "UPDATE t_department_permission SET has = 1 WHERE department_id = ? AND permission_id IN (" + inSql + ")";

        // 构造参数列表
        List<Object> params = new ArrayList<>();
        params.add(departmentId);
        params.addAll(permissionIds);

        // 执行更新
        return jdbc.update(sql, params.toArray()) > 0;
    }
}
