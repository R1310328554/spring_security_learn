package com.okta.spring.jwt.domain;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UmsAdminMapper {

    private ConcurrentHashMap<String , UmsAdmin> users = new ConcurrentHashMap();
    {
        UmsAdmin admin = new UmsAdmin();
        admin.setId(1L);
        admin.setUsername("a");
        admin.setPassword("1");
        users.put("a", admin);

        UmsAdmin admin2 = new UmsAdmin();
        admin2.setId(2L);
        admin2.setUsername("b");
        admin2.setPassword("1");
        users.put("b", admin2);

    }

    public List<UmsAdmin> selectByExample(String username) {
        return Collections.singletonList(users.get(username));
    }

    public void insert(UmsAdmin umsAdmin) {
        users.put(umsAdmin.getUsername(), umsAdmin);
    }
}
