package com.lk.learn.dynamicAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserMapperDao userMapperDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapperDao.loadUserByUsername(username);
          if (user == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }
         // 我的数据库用户密码没加密，这里手动设置
        String encodePassword = passwordEncoder.encode(user.getPassword());
        System.out.println("加密后的密码：" + encodePassword);// 简单起见，数据库中存明文密码即可
        user.setPassword(encodePassword);
        List<Role> userRoles = userMapperDao.getUserRolesByUid(user.getId());
        user.setUserRoles(userRoles);
        return user;
    }
}
