package com.lk.learn.dynamicAuth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 新动态权限控制方法！参考 https://juejin.cn/post/7099426676777418760
 */
@Component("permissionService")
public class PermissionService {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //拿到认证的主体
        Object principal = authentication.getPrincipal();
        //是否为UserDetails类型
        if (principal instanceof UserDetails) {
            //做一次强转
            UserDetails userDetails = (UserDetails) principal;

            //简单授权规则把uri填进去
            //todo ， 使用 OAuth2UserAuthority 更好
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(request.getRequestURI());
            //判断是否含有该规则
            // todo 不能直接url和 角色做比较
            return userDetails.getAuthorities().contains(simpleGrantedAuthority);
        }
        return false;
    }
}
