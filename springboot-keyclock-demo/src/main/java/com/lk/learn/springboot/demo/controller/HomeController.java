package com.lk.learn.springboot.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HomeController {

//    private LoginService loginService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/customer")
    public String customer() {
        System.out.println("HomeController.customer");
        return "only customer can see";
    }

    @RequestMapping("/admin")
    public String admin() {
        System.out.println("HomeController.admin");
        return "only admin cas see";
    }

    @RequestMapping("/test")
    public String test() {
        System.out.println("HomeController.test    " + System.currentTimeMillis());
        return "test " + System.currentTimeMillis();
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    /*
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody, HttpServletResponse httpServletResponse) throws IOException {
        AjaxResult ajax = AjaxResult.success();
        // TODO 查询keyclock的用户是否存在与数据库，不存在则添加
        SysUser sysUser = sysUserService.selectUserByUserName(loginBody.getUsername());
        // 生成令牌（整合keyclock后，这个令牌就不用了，但是方法里面面的内容还有用）
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        // 这里直接返回keycloak生成的token
        ajax.put(Constants.TOKEN, loginBody.getCode());
        return ajax;
    }*/

}
