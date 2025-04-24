package com.liwei.ruiyi.service.impl;

import com.liwei.ruiyi.bo.TUser;
import com.liwei.ruiyi.dao.TUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    TUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查用户
        TUser user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 2. 查角色
//        List<String> roles = userDao.findRolesByUsername(username);
        List<String> roles = userDao.findDepartmentsByUsername(username);
        List<SimpleGrantedAuthority> auths = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
        // 3. 返回 Spring Security 的 User 对象
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 已经是 BCrypt 加密后的
                .authorities(auths)
                .accountLocked(user.getIsBan() == 1)
                .build();
    }
}
