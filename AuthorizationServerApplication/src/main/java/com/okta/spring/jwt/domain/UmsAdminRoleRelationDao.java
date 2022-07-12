package com.okta.spring.jwt.domain;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 后台用户与角色管理自定义Dao
 * Created by macro on 2018/10/8.
 */
@Service
public class UmsAdminRoleRelationDao {


    private ConcurrentHashMap<Long , UmsPermission> map = new ConcurrentHashMap();

    {
        UmsPermission umsPermmision = new UmsPermission();
        umsPermmision.setId(1L);
        umsPermmision.setName("umsPermmisionA");
        map.put(1L, umsPermmision);
    }

    /**
     * 获取用户所有权限(包括+-权限)
     */
    List<UmsPermission> getPermissionList( Long adminId) {
        return Collections.singletonList(map.get(adminId));
    }



}
