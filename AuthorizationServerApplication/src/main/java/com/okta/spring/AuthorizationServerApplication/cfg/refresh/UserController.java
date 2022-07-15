//package com.okta.spring.AuthorizationServerApplication.cfg.refresh;
//
//import com.common.model.domain.QQUser;
//import com.common.model.ext.QQUserAuth;
//import com.common.model.request.QQUserQuery;
//import com.common.model.response.ResponseResult;
//import com.user.resource.server.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 获取用户信息接口入口
// */
//@RestController
//public class UserController {
//
//    @Autowired
//    UserService userService;
//
//    /**
//     * 根据用户名获取认证用户信息（不要求用户已认证，即不要求有访问令牌）
//     */
//    @PostMapping("/getQQUserAuth")
//    public ResponseResult getQQUserAuth(@RequestBody QQUserQuery query) {
//        QQUserAuth user = null;
//        if(query == null) {
//            return ResponseResult.getFailureResult("300", "没有接收到用户信息");
//        } else if(!StringUtils.isEmpty(query.getUsername())) {
//            user = userService.getQQUserAuthByUsername(query.getUsername());
//        }
//        if(user == null) {
//            return ResponseResult.getFailureResult("300", "没有查询到该用户信息");
//        } else {
//            return ResponseResult.getSuccessResult().put("QQUserAuth", user);
//        }
//    }
//
//    /**
//     * 已认证用户查询自己的用户信息，不允许查询他人的用户信息（要求用户已认证，即要求有合法的访问令牌）
//     */
//    @PostMapping("/api/getQQUser")
//    @PreAuthorize("principal.equals(#query.getUsername())")
//    public ResponseResult getQQUser(@RequestBody QQUserQuery query) {
//        QQUser user = null;
//        if(query == null) {
//            return ResponseResult.getFailureResult("300", "没有接收到用户信息");
//        } else if(!StringUtils.isEmpty(query.getUsername())) {
//            user = userService.getQQUserByUsername(query.getUsername());
//        }
//        if(user == null) {
//            return ResponseResult.getFailureResult("300", "没有获取到该用户信息");
//        } else {
//            return ResponseResult.getSuccessResult().put(user);
//        }
//    }
//
//}
