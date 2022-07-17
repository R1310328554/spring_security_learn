package com.lk.learn.dynamicAuth;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;

/**
 * 资源权限评估; 另外的 动态权限控制方法！参考 https://juejin.cn/post/7088309669646565412
 *
 * @author felord.cn
 */
public class ResourcePermissionEvaluator implements PermissionEvaluator {
    private final BiFunction<String, String, Collection<? extends GrantedAuthority>> permissionFunction;

    public ResourcePermissionEvaluator(BiFunction<String, String, Collection<? extends GrantedAuthority>> permissionFunction) {
        this.permissionFunction = permissionFunction;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        //查询方法标注对应的角色
        Collection<? extends GrantedAuthority> resourceAuthorities = permissionFunction.apply((String) targetDomainObject, (String) permission);
        // 用户对应的角色
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
         // 对比 true 就能访问  false 就不能访问
        return userAuthorities.stream().anyMatch(resourceAuthorities::contains);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        //todo
        System.out.println("targetId = " + targetId);
        return true;
    }
}
