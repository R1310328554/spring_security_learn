//package com.lk.learn.dynamicAuth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class CatDetailsService implements UserDetailsService {
//
//    @Autowired
//    private CatService catService;
//
//    @Override
//    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        //查询用户基本信息
//        CatDetails catDetails = catService.getCatDetailsByName(name);
//
//        //查询当前猫猫的角色列表
//        List<String> roleCodeList = catService.getRoleCodeList(catDetails.getName());
//
//        //根据当前猫猫的角色列表查询所拥有权限的api
//        List<String> authorities = catService.getPermissionApi(roleCodeList);
//
//        //添加标识前缀
//        roleCodeList = roleCodeList.stream().map(roleCode -> "ROLE_" + roleCode).collect(Collectors.toList());
//        authorities.addAll(roleCodeList); //角色也是权限，所以要添加到权限列表中
//
//        catDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(
//                String.join(",", authorities)
//        ));
//
//        log.info(catDetails.toString());
//        return catDetails;
//    }
//}
