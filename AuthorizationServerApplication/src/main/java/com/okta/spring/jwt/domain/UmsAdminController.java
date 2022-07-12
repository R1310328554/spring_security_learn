package com.okta.spring.jwt.domain;

import com.okta.spring.jwt.domain.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户管理
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader:Authorization}")
    private String tokenHeader;
    @Value("${jwt.tokenHead:Bearer}")
    private String tokenHead;

    @ApiOperation(value = "登录以后返回token")
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam, BindingResult result) {
    @RequestMapping(value = "/login") // http://192.168.1.103:8081/auth/admin/login?username=a&password=1
    @ResponseBody
    public CommonResult login(UmsAdminLoginParam umsAdminLoginParam, BindingResult result) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
        /*
            结果：

            {"code":200,"message":"操作成功","data":{"tokenHead":"Bearer","token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhIiwiaWF0IjoxNjU3NTA2MjUyLCJ1c2VyaWQiOiJhIn0.PzB7F-bADezzo_fmmY37qIhRppEAD-bD6qSBEw_rooc"}}

         */
    }


    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdmin umsAdminParam, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (umsAdmin == null) {
            CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable Long adminId) {
        List<UmsPermission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }


    @RequestMapping(value = "/test")
    @ResponseBody
    public CommonResult test(Model model) {
        System.out.println("UmsAdminController.test     " + model);

        return CommonResult.success("success! ");
    }

}
