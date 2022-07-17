
package com.lk.learn.dynamicAuth;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MethodService {

    // 访问该方法需要 ADMIN 角色。注意：这里需要在角色前加一个前缀"ROLE_"
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "hello admin";
    }

    // 访问该方法既要 ADMIN 角色，又要 DBA 角色
    @PreAuthorize("hasRole('ADMIN') and hasRole('DBA')")
    public String dba() {
        return "hello dba";
    }

    // 访问该方法只需要 ADMIN、DBA、USER 中任意一个角色即可
    @PreAuthorize("hasAnyRole('ADMIN','DBA','USER')")
    public String user() {
        return "hello user";
    }
}
